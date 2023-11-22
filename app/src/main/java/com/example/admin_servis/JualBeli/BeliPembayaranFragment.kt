package com.example.admin_servis.JualBeli

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.Helper.PhotoHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.beli_pembayaran_fragment.view.*
import org.json.JSONObject

class BeliPembayaranFragment : BottomSheetDialogFragment() {
    lateinit var thisParent: BeliDetailActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var imStr = ""
    var fileUri = Uri.parse("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as BeliDetailActivity
        v = inflater.inflate(R.layout.beli_pembayaran_fragment, container, false)

        urlClass = UrlClass()
        val bundle = arguments
        v.beliBayarKodeTransaksi.setText("Kode transaksi : "+bundle?.getString("kode").toString())
        v.beliBayarTotal.setText(bundle?.getString("hrg").toString())

        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        v.btnChoosePhotoBeli.setOnClickListener {
            requestPermission()
        }

        v.btnBatalkan.setOnClickListener { dismiss() }

        v.btnTutup.setOnClickListener { dismiss() }

        v.btnBeliKirimBayar.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin melakukan pembayaran pada pembelian ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    bayarBeli("bayar_beli")
                    Toast.makeText(this.context,"Berhasil melakukan pembayaran!", Toast.LENGTH_LONG).show()
                    thisParent.recreate()
                    dismiss()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }
        return v
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(v.beliBayarBuktiImage, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    fun bayarBeli(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if(respon.equals("1")){
//                    Toast.makeText(this.context,"Berhasil melakukan pembayaran Pembelian barang!", Toast.LENGTH_LONG).show()
//                }else {
//                    Toast.makeText(this.context,"Gagal melakukan pembayaran Pembelian barang!", Toast.LENGTH_LONG).show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "bayar_beli" -> {
                        hm.put("mode", "bayar_beli")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("kd_pembayaran", thisParent.kdP)
                        hm.put("kd_user", thisParent.kdU)
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}