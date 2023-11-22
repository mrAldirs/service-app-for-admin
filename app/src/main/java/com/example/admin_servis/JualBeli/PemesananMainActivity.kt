package com.example.admin_servis.JualBeli

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterOrder
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.pemesanan_main_activity.*
import org.json.JSONArray
import org.json.JSONObject

class PemesananMainActivity : AppCompatActivity() {
    lateinit var urlClass: UrlClass

    val datftarTran = mutableListOf<HashMap<String,String>>()
    lateinit var tranAdapter: AdapterOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemesanan_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)

        urlClass = UrlClass()
        tranAdapter = AdapterOrder(datftarTran, this)
        rvPemesananBarang.layoutManager = LinearLayoutManager(this)
        rvPemesananBarang.adapter = tranAdapter
    }

    override fun onStart() {
        super.onStart()
        readTransaksi("show_data_order")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun readTransaksi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                datftarTran.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("tgl_transaksi",jsonObject.getString("tgl_transaksi"))
                        frm.put("nama_barang",jsonObject.getString("nama_barang"))
                        frm.put("status_transaksi",jsonObject.getString("status_transaksi"))
                        frm.put("kd_transaksi",jsonObject.getString("kd_transaksi"))
                        frm.put("kd_barang",jsonObject.getString("kd_barang"))

                        datftarTran.add(frm)
                    }
                    tranAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(this)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_order" -> {
                        hm.put("mode","show_data_order")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun delete(kdT: String, kdB: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    readTransaksi("show_data_order")
                    Toast.makeText(this,"Berhasil menghapus data!", Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "delete")
                hm.put("kd_transaksi", kdT)
                hm.put("kd_barang", kdB)

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}