package com.example.admin_servis.JualBeli

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.Helper.MediaHelper
import com.example.admin_servis.Helper.PhotoHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.beli_konfirmasi_fragment.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BeliKonfirmasiFragment : BottomSheetDialogFragment() {
    lateinit var v:View
    lateinit var thisParent : BeliDetailActivity
    lateinit var urlClass: UrlClass

    var konfirmasiText = ""

    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var fileUri = Uri.parse("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as BeliDetailActivity
        v = inflater.inflate(R.layout.beli_konfirmasi_fragment, container, false)

        urlClass = UrlClass()
        val bundle = arguments
        v.konfirmasiBeliKodeTransaksi.setText("Kode transaksi : "+bundle?.getString("kode").toString())
        v.konfirmasiPayment.setText(bundle?.getString("pay").toString())

        mediaHealper = MediaHelper(v.context)
        photoHelper = PhotoHelper()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        v.btnChoosePhotoBeli.setOnClickListener {
            var photoMenu = PopupMenu(v.context, it)
            photoMenu.menuInflater.inflate(R.menu.menu_photo, photoMenu.menu)
            photoMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuPhotoRealtime -> {
                        requestPermission()
                    }
                    R.id.menuPhotoManager -> {
                        val intent = Intent()
                        intent.setType("image/*")
                        intent.setAction(Intent.ACTION_GET_CONTENT)
                        startActivityForResult(intent,mediaHealper.RcGallery())
                    }
                }
                false
            }
            photoMenu.show()
        }

        if (v.konfirmasiPayment.text.toString().equals("COD")) {
            v.tvBayar.visibility = View.GONE
            v.cardBayar.visibility = View.GONE
        }

        v.btnBatalkan.setOnClickListener { dismiss() }

        v.btnTutup.setOnClickListener { dismiss() }

        v.konfirmasiBeliJam.setOnClickListener {
            showTimePickerDialog()
        }

        v.konfirmasiBeliTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        v.rgKonfirmasiBeli.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.konfirmasiTerimaBeli -> {
                    konfirmasiText = "terima"
                    v.textKonfJam.visibility = View.VISIBLE
                }
                R.id.konfirmasiTolakBeli -> {
                    konfirmasiText = "tolak"
                    v.konfirmasiBeliJam.setText("")
                    v.konfirmasiBeliTanggal.setText("")
                    v.textKonfJam.visibility = View.GONE
                }
            }
        }

        v.btnBeliKirimKonfirmasi.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda yakin ingin membeli barang ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!v.konfirmasiBeliCatatan.text.toString().equals("")) {
                        if (konfirmasiText.equals("terima")) {
                            konfirmasiBeli("konfirmasi_diterima")
                            Toast.makeText(this.context,"Berhasil membeli barang!", Toast.LENGTH_LONG).show()
                        } else {
                            konfirmasiBeli("konfirmasi_ditolak")
                            Toast.makeText(this.context,"Anda membatalkan pembelian barang ini!", Toast.LENGTH_LONG).show()
                        }
                        thisParent.recreate()
                        dismiss()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            v.context,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                v.konfirmasiBeliJam.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            v.context,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                v.konfirmasiBeliTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
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
            mediaHealper.RcGallery() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = mediaHealper.getBitmapToString(data!!.data,v.beliBayarBuktiImage)
                        namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                            Date()
                        )+".jpg"
                    }
                    AppCompatActivity.RESULT_CANCELED -> {

                    }
                }
            }
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

    private fun konfirmasiBeli(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "konfirmasi_diterima" -> {
                        hm.put("mode", "konfirmasi_beli")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("status_transaksi", "Proses")
                        hm.put("payment", v.konfirmasiPayment.text.toString())
                        hm.put("tgl_ambil", v.konfirmasiBeliTanggal.text.toString()+" "+v.konfirmasiBeliJam.text.toString())
                        hm.put("catatan_transaksi", v.konfirmasiBeliCatatan.text.toString())
                        hm.put("kd_user", thisParent.kdU)
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                    "konfirmasi_ditolak" -> {
                        hm.put("mode", "konfirmasi_beli")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("status_transaksi", "Tolak")
                        hm.put("catatan_transaksi", v.konfirmasiBeliCatatan.text.toString())
                        hm.put("kd_user", thisParent.kdU)
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}