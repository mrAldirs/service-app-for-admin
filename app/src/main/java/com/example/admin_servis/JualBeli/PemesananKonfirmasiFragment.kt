package com.example.admin_servis.JualBeli

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Helper.BaseApplication
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.pemesanan_konfirmasi_fragment.view.*
import org.json.JSONObject

class PemesananKonfirmasiFragment : BottomSheetDialogFragment() {
    lateinit var thisParent : PemesananDetailActivity
    lateinit var v : View
    lateinit var urlClass : UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PemesananDetailActivity
        v = inflater.inflate(R.layout.pemesanan_konfirmasi_fragment, container, false)

        urlClass = UrlClass()

        v.btnBatalkan.setOnClickListener { dismiss() }

        v.btnTutup.setOnClickListener { dismiss() }

        v.btnOrderKirimKonfirmasi.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin mengonfirmasi pre-order ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!v.konfirmasiCatatan.text.toString().equals("")){
                        konfirmasiOrder()
                    } else {
                        Toast.makeText(v.context, "Tolong isi catatan terlebih dahulu!", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    fun konfirmasiOrder() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if(respon.equals("1")){
                    dismiss()
                    thisParent.detailOrder("detail_order")
                    Toast.makeText(thisParent,"Berhasil mengonfirmasi pre-order", Toast.LENGTH_LONG).show()
                    BaseApplication.notificationHelper.notifTransaksi("Admin telah mengonfirmasi pre-order barang Anda. Silahkan cek barang di katalog toko!")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "konfirmasi_order")
                hm.put("kd_transaksi", thisParent.kdT)
                hm.put("status_transaksi", "Selesai")
                hm.put("catatan_transaksi", v.konfirmasiCatatan.text.toString())
                hm.put("kd_user", thisParent.kdU)

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}