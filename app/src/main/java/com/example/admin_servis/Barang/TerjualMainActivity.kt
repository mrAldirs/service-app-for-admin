package com.example.admin_servis.Barang

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
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
import com.example.admin_servis.View.Barang.BarangDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.brgterjual_main_activity.*
import org.json.JSONArray
import org.json.JSONObject

class TerjualMainActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    var kdBrg = ""

    val daftarBarang = mutableListOf<HashMap<String, String>>()
    lateinit var barangAdapter: AdapterKatalog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.brgterjual_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)

        urlClass = UrlClass()
        barangAdapter = AdapterKatalog(daftarBarang, this)
        rvBarangTerjual.layoutManager = LinearLayoutManager(this)
        rvBarangTerjual.adapter = barangAdapter

        btnSearchBrgTerjual.setOnClickListener {
            readBarang("read_all_terjual", textSearchBrgTerjual.text.toString().trim())
        }
    }

    override fun onStart() {
        super.onStart()
        readBarang("read_all_terjual", "")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun readBarang(mode: String, nama_barang: String) {
        val request = object : StringRequest(
            Method.POST, urlClass.urlKatalog,
            Response.Listener { response ->
                daftarBarang.clear()
                if (response.equals(0)) {
                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String, String>()
                        frm.put("nama_barang", jsonObject.getString("nama_barang"))
                        frm.put("tgl_upload", jsonObject.getString("tgl_upload"))
                        frm.put("jenis_barang", jsonObject.getString("jenis_barang"))
                        frm.put("status_katalog", jsonObject.getString("status_katalog"))
                        frm.put("img_barang", jsonObject.getString("img_barang"))
                        frm.put("kd_barang", jsonObject.getString("kd_barang"))
                        frm.put("kd_katalog", jsonObject.getString("kd_katalog"))
                        frm.put("kd_transaksi", jsonObject.getString("kd_transaksi"))

                        daftarBarang.add(frm)
                    }
                    barangAdapter.notifyDataSetChanged()
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
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when (mode) {
                    "read_all_terjual" -> {
                        hm.put("mode", "read_all_terjual")
                        hm.put("nama_barang", nama_barang)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterKatalog(val dataKatalog: List<HashMap<String,String>>, val parent: TerjualMainActivity) :
        RecyclerView.Adapter<AdapterKatalog.HolderDataKatalog>(){
        class HolderDataKatalog(v: View) : RecyclerView.ViewHolder(v){
            val cd = v.findViewById<CardView>(R.id.card)
            val nmB = v.findViewById<TextView>(R.id.tersediaNama)
            val tgl = v.findViewById<TextView>(R.id.tersediaTanggal)
            val jns = v.findViewById<TextView>(R.id.tersediaJenis)
            val sts = v.findViewById<TextView>(R.id.tersediaStatusKatalog)
            val img = v.findViewById<ImageView>(R.id.tersediaPhoto)
            val edt = v.findViewById<Button>(R.id.btnEdit)
            val hps = v.findViewById<Button>(R.id.btnHapus)
            val dtt = v.findViewById<Button>(R.id.btnDetailTransaksi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKatalog {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_katalog, parent, false)
            return HolderDataKatalog(v)
        }

        override fun getItemCount(): Int {
            return dataKatalog.size
        }

        override fun onBindViewHolder(holder: HolderDataKatalog, position: Int) {
            val data = dataKatalog.get(position)

            holder.nmB.setText(data.get("nama_barang"))
            holder.tgl.setText(data.get("tgl_upload"))
            holder.jns.setText(data.get("jenis_barang"))
            Picasso.get().load(data.get("img_barang")).into(holder.img)

            val stat = data.get("status_katalog")
            if (stat.toString().equals("Ready")) {
                holder.sts.setText("Barang Tersedia")
                holder.dtt.visibility = View.GONE
                holder.sts.setTextColor(Color.parseColor("#2046FF"))
            } else if (stat.toString().equals("Pending")) {
                holder.sts.setText("Transaksi Pending")
                holder.sts.setTextColor(Color.parseColor("#EC0D0D"))
                holder.edt.visibility = View.GONE
                holder.hps.visibility = View.GONE
            } else {
                holder.sts.setText("Barang Telah Terjual")
                holder.sts.setTextColor(Color.parseColor("#17ad96"))
                holder.edt.visibility = View.GONE
                holder.hps.visibility = View.GONE
            }

            var kdB = data.get("kd_barang")

            holder.dtt.setOnClickListener { v: View ->
                val intent = Intent(v.context, BarangDetailActivity::class.java)
                intent.putExtra("kdB", kdB)
                intent.putExtra("kdT", data.get("kd_transaksi").toString())
                v.context.startActivity(intent)
            }

            holder.dtt.setText("Detail Barang")
            holder.dtt.setBackgroundColor(Color.parseColor("#FF2196F3"))
        }
    }
}