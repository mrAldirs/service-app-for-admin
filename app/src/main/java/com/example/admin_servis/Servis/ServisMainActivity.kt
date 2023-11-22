package com.example.admin_servis.Servis

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.View.Servis.ServisDetailActivity
import kotlinx.android.synthetic.main.servis_main_activity.*
import org.json.JSONArray
import org.json.JSONObject

class ServisMainActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass

    val daftarServis = mutableListOf<HashMap<String,String>>()
    lateinit var servisAdapter: AdapterServis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.servis_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        urlClass = UrlClass()
        servisAdapter = AdapterServis(daftarServis, this)
        rvServis.layoutManager = LinearLayoutManager(this)
        rvServis.adapter = servisAdapter

        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)
    }

    override fun onStart() {
        super.onStart()
        readServisProses("read_all_servis")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun readServisProses(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlServis,
            Response.Listener { response ->
                daftarServis.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("tgl_transaksi",jsonObject.getString("tgl_transaksi"))
                        frm.put("kerusakan",jsonObject.getString("kerusakan"))
                        frm.put("kd_servis",jsonObject.getString("kd_servis"))
                        frm.put("status_transaksi",jsonObject.getString("status_transaksi"))
                        frm.put("kd_transaksi",jsonObject.getString("kd_transaksi"))
                        frm.put("kd_barang",jsonObject.getString("kd_barang"))

                        daftarServis.add(frm)
                    }
                    servisAdapter.notifyDataSetChanged()
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
                    "read_all_servis" -> {
                        hm.put("mode","read_all_servis")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun delete(kdT: String, kdB: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    readServisProses("read_all_servis")
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

    class AdapterServis(private val dataServis: List<HashMap<String,String>>, val parent: ServisMainActivity)
        : RecyclerView.Adapter<AdapterServis.HolderDataServis>() {
        class HolderDataServis(v: View) : RecyclerView.ViewHolder(v) {
            val namaServis = v.findViewById<TextView>(R.id.adpNamaServis)
            val tanggal = v.findViewById<TextView>(R.id.adpTanggalKirimServis)
            val kerusakan = v.findViewById<TextView>(R.id.adpKerusakanServis)
            val jenis = v.findViewById<TextView>(R.id.adpKodeServis)
            val status = v.findViewById<TextView>(R.id.adpStatusServis)
            val card = v.findViewById<CardView>(R.id.cardServis)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataServis {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_servis, parent, false)
            return HolderDataServis(v)
        }

        override fun getItemCount(): Int {
            return dataServis.size
        }

        override fun onBindViewHolder(holder: HolderDataServis, position: Int) {
            val data = dataServis.get(position)

            holder.namaServis.setText(data.get("nama"))
            holder.tanggal.setText(data.get("tgl_transaksi"))
            holder.kerusakan.setText(data.get("kerusakan"))
            holder.jenis.setText(data.get("kd_servis"))

            val status = data.get("status_transaksi")
            if (status.toString().equals("Belum")) {
                holder.status.setText("baru")
            } else if (status.toString().equals("Proses")){
                holder.status.setText("proses")
                holder.status.setTextColor(Color.parseColor("#000000"))
            }
//            else {
//                if (data.get("status_transaksi").toString().equals("selesai")) {
//                    holder.status.setTextColor(Color.parseColor("#17ad96"))
//                    holder.status.setText("selesai")
//                } else {
//                    holder.status.setTextColor(Color.RED)
//                    holder.status.setText("selesai")
//                }
//
//                holder.card.setOnLongClickListener {
//                    val contextMenu = PopupMenu(parent, it)
//                    contextMenu.menuInflater.inflate(R.menu.context_hapus, contextMenu.menu)
//                    contextMenu.setOnMenuItemClickListener {
//                        when (it.itemId) {
//                            R.id.contextHapus -> {
//                                AlertDialog.Builder(parent)
//                                    .setIcon(R.drawable.warning)
//                                    .setTitle("Peringatan!")
//                                    .setMessage("Apakah Anda menghapus transaksi ini?")
//                                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
//                                        parent.delete(data.get("kd_transaksi").toString(), data.get("kd_barang").toString())
//                                    })
//                                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
//                                    })
//                                    .show()
//                                true
//                            }
//                        }
//                        false
//                    }
//                    contextMenu.show()
//                    true
//                }
//            }

            holder.card.setOnClickListener {
                val intent = Intent(it.context, ServisDetailActivity::class.java)
                intent.putExtra("kode", data.get("kd_transaksi").toString())
                it.context.startActivity(intent)
            }
        }
    }
}