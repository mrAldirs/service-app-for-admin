package com.example.admin_servis.Akun

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterAkun
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.akun_main_activity.*
import org.json.JSONArray

class AkunMainActivity : AppCompatActivity() {

    lateinit var akunInsert: FragmentAkunInsert
    lateinit var ft : FragmentTransaction

    lateinit var urlClass: UrlClass

    val daftarAkun = mutableListOf<HashMap<String,String>>()
    lateinit var akunAdapter: AdapterAkun

    var kdAkun = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.akun_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)

        urlClass = UrlClass()
        akunAdapter = AdapterAkun(daftarAkun)
        rvAkun.layoutManager = LinearLayoutManager(this)
        rvAkun.adapter = akunAdapter

        btnCari.setOnClickListener {
            showDataAkun("show_data_akun", txCari.text.toString().trim())
        }

        btnInsertAkun.setOnClickListener {
            ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayoutInsertAkun, akunInsert).commit()
            frameLayoutInsertAkun.setBackgroundColor(Color.argb(255,255,255,255))
            frameLayoutInsertAkun.visibility = View.VISIBLE
            btnInsertAkun.visibility = View.GONE
        }

        akunInsert = FragmentAkunInsert()
    }

    override fun onStart() {
        super.onStart()
        showDataAkun("show_data_akun", "")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun showDataAkun(mode: String, nama: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                daftarAkun.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("kd_akun",jsonObject.getString("kd_akun"))
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("username",jsonObject.getString("username"))
                        frm.put("password",jsonObject.getString("password"))
                        frm.put("sts_akun",jsonObject.getString("sts_akun"))
                        frm.put("level",jsonObject.getString("level"))

                        daftarAkun.add(frm)
                    }
                    akunAdapter.notifyDataSetChanged()
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
                    "show_data_akun" -> {
                        hm.put("mode","show_data_akun")
                        hm.put("nama", nama)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}