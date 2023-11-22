package com.example.admin_servis.Informasi

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.informasi_main_activity.*
import org.json.JSONArray
import org.json.JSONObject

class InformasiMainActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass

    val daftarNotifikasi = mutableListOf<HashMap<String,String>>()
    lateinit var notifikasiAdapter: AdapterNotifikasi

    var kdNotif = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.informasi_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)

        urlClass = UrlClass()
        notifikasiAdapter = AdapterNotifikasi(daftarNotifikasi, this)
        rvInformasi.layoutManager = LinearLayoutManager(this)
        rvInformasi.adapter = notifikasiAdapter

        btnInsertInformasi.setOnClickListener {
            val frag = FragmentInformasiInsert()

            frag.show(supportFragmentManager, "FragmentInfromasiInsert")
        }
    }

    override fun onStart() {
        super.onStart()
        readAllNotifikasi("read_notifikasi_informasi")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun readAllNotifikasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlNotifikasi,
            Response.Listener { response ->
                daftarNotifikasi.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("jamtanggal_notif",jsonObject.getString("jamtanggal_notif"))
                        frm.put("kd_user",jsonObject.getString("kd_user"))
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("jenis_notif",jsonObject.getString("jenis_notif"))
                        frm.put("teks_notif",jsonObject.getString("teks_notif"))
                        frm.put("kd_notif",jsonObject.getString("kd_notif"))

                        daftarNotifikasi.add(frm)
                    }
                    notifikasiAdapter.notifyDataSetChanged()
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
                    "read_notifikasi_informasi" -> {
                        hm.put("mode","read_notifikasi_informasi")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun delete(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlNotifikasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if(respon.equals("1")){
//                    Toast.makeText(this,"Berhasil menghapus Informasi!", Toast.LENGTH_LONG).show()
//                }else {
//                    Toast.makeText(this,"Gagal menghapus Informasi!", Toast.LENGTH_LONG).show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "delete" -> {
                        hm.put("mode", "delete")
                        hm.put("kd_notif", kdNotif)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterNotifikasi(val dataNotif: List<HashMap<String,String>>, val parent: InformasiMainActivity)
        : RecyclerView.Adapter<AdapterNotifikasi.HolderDataNotifikasi>(){
        class HolderDataNotifikasi(v: View) : RecyclerView.ViewHolder(v) {
            val jamTanggal = v.findViewById<TextView>(R.id.adpJamTanggalNotif)
            val jenisNotif = v.findViewById<TextView>(R.id.adpJenisNotif)
            val teks = v.findViewById<TextView>(R.id.adpTeksNotif)
            val send = v.findViewById<TextView>(R.id.adpNamaUserNotif)
            val card = v.findViewById<CardView>(R.id.cardNotif)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataNotifikasi {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_notif,parent,false)
            return HolderDataNotifikasi(v)
        }

        override fun getItemCount(): Int {
            return dataNotif.size
        }

        override fun onBindViewHolder(holder: HolderDataNotifikasi, position: Int) {
            val data = dataNotif.get(position)
            holder.jamTanggal.setText(data.get("jamtanggal_notif"))
            holder.jenisNotif.setText(data.get("jenis_notif") + "!")
            holder.teks.setText(data.get("teks_notif"))

            val sendNotif = data.get("kd_user")
            if (sendNotif.toString().equals("All")) {
                holder.send.setText("Dikirim ke: Semua")
            } else {
                holder.send.setText("")
            }

            holder.card.setOnLongClickListener {
                parent.kdNotif = data.get("kd_notif").toString()

                val contextMenu = PopupMenu(parent, it)
                contextMenu.menuInflater.inflate(R.menu.context_informasi, contextMenu.menu)
                contextMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.informasiEdit -> {
                            var frag = FragmentInformasiEdit()
                            var paket : Bundle? = parent.intent.extras
                            var nm = paket?.getString("nama")
                            var teks = paket?.getString("teks")
                            var kd = paket?.getString("kode")

                            val bundle = Bundle()
                            bundle.putString("kode", kd)
                            bundle.putString("kode", data.get("kd_notif").toString())
                            bundle.putString("nama", nm)
                            bundle.putString("nama", data.get("nama").toString())
                            bundle.putString("teks", teks)
                            bundle.putString("teks", data.get("teks_notif").toString())
                            frag.arguments = bundle

                            frag.show(parent.supportFragmentManager, "FragmentInformasiEdit")
                        }
                        R.id.informasiHapus -> {
                            AlertDialog.Builder(parent)
                                .setIcon(R.drawable.warning)
                                .setTitle("Peringatan!")
                                .setMessage("Apakah Anda menghapus informasi ini?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                    parent.delete("delete")
                                    parent.recreate()
                                    Toast.makeText(parent,"Berhasil menghapus Informasi!", Toast.LENGTH_LONG).show()
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                })
                                .show()
                            true
                        }
                    }
                    false
                }
                contextMenu.show()
                true
            }

            holder.card.setOnClickListener {
                var frag = FragmentInformasiDetail()
                var paket : Bundle? = parent.intent.extras
                var jns = paket?.getString("jenis")
                var tgl = paket?.getString("tanggal")
                var teks = paket?.getString("teks")

                val bundle = Bundle()
                bundle.putString("jenis", jns)
                bundle.putString("jenis", data.get("jenis_notif").toString())
                bundle.putString("tanggal", tgl)
                bundle.putString("tanggal", data.get("jamtanggal_notif").toString())
                bundle.putString("teks", teks)
                bundle.putString("teks", data.get("teks_notif").toString())
                frag.arguments = bundle

                frag.show(parent.supportFragmentManager, "FragmentInformasiDetail")
            }
        }
    }
}