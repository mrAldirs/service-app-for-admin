package com.example.admin_servis.JualBeli

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterBeli
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.transaksi_jualbeli_fragment.view.*
import org.json.JSONArray
import org.json.JSONObject

class FragmentTransaksiBeli : Fragment() {
    lateinit var thisParent: TransaksiMainActivity
    lateinit var v : View
    lateinit var urlClass: UrlClass

    val datftarTran = mutableListOf<HashMap<String,String>>()
    lateinit var tranAdapter: AdapterBeli

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as TransaksiMainActivity
        v = inflater.inflate(R.layout.transaksi_jualbeli_fragment, container, false)

        urlClass = UrlClass()
        tranAdapter = AdapterBeli(datftarTran, this)
        v.rvTransaksiJualBeli.layoutManager = LinearLayoutManager(v.context)
        v.rvTransaksiJualBeli.adapter = tranAdapter

        v.btnSearchJualBeliTransaksi.setOnClickListener {
            readTransaksi("show_data_beli", v.textSearchJualBeliTransaksi.text.toString().trim())
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        readTransaksi("show_data_beli", "")
    }

    private fun readTransaksi(mode: String, nama_barang: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                datftarTran.clear()
                if (response.equals(0)) {
                    Toast.makeText(v.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("total_bayar",jsonObject.getString("total_bayar"))
                        frm.put("nama_barang",jsonObject.getString("nama_barang"))
                        frm.put("status_transaksi",jsonObject.getString("status_transaksi"))
                        frm.put("img_barang",jsonObject.getString("img_barang"))
                        frm.put("kd_transaksi",jsonObject.getString("kd_transaksi"))
                        frm.put("kd_barang",jsonObject.getString("kd_barang"))

                        datftarTran.add(frm)
                    }
                    tranAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(v.context)
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
                    "show_data_beli" -> {
                        hm.put("mode","show_data_beli")
                        hm.put("nama_barang", nama_barang)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun delete(kdT: String, kdB: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    readTransaksi("show_data_beli", "")
                    Toast.makeText(v.context,"Berhasil menghapus data!", Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "delete")
                hm.put("kd_transaksi", kdT)
                hm.put("kd_barang", kdB)

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }
}