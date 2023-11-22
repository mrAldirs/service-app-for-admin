package com.example.admin_servis.Feedback

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterFeedback
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.feedback_main_activity.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackMainActivity : AppCompatActivity() {

    val dataFeedback = mutableListOf<HashMap<String,String>>()
    lateinit var feedbackAdp : AdapterFeedback
    lateinit var urlClass: UrlClass

    var teks = ""
    var idF = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)

        urlClass = UrlClass()

        feedbackAdp = AdapterFeedback(dataFeedback, this)
        rvFeedback.adapter = feedbackAdp
        rvFeedback.layoutManager = LinearLayoutManager(this)
        rvFeedback.layoutManager?.scrollToPosition(feedbackAdp.itemCount - 1)

        btnKirimFeedback.setOnClickListener {
            if (teksReplyMain.text.toString().equals("") && namaReplyMain.text.toString().equals("")) {
                teks = txPesanFeedback.text.toString()
                kirimFeedback("insert")
            } else {
                teks = txPesanFeedback.text.toString()
                kirimFeedback("insert_reply")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dataFeedback("show_data_feedback")
        rvFeedback.layoutManager?.let {
            val lastItemPosition = rvFeedback.adapter?.itemCount?.minus(1) ?: 0
            it.scrollToPosition(lastItemPosition)
        }
    }

    fun restartActivity() {
        recreate()
        txPesanFeedback.clearFocus()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun dataFeedback(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlFeedback,
            Response.Listener { response ->
                dataFeedback.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    var  bkt = java.util.HashMap<String, String>()
                    bkt.put("kd_feedback",jsonObject.getString("kd_feedback"))
                    bkt.put("teks_feedback",jsonObject.getString("teks_feedback"))
                    bkt.put("nama",jsonObject.getString("nama"))
                    bkt.put("nama_reply",jsonObject.getString("nama_reply"))
                    bkt.put("teks_reply",jsonObject.getString("teks_reply"))
                    bkt.put("level",jsonObject.getString("level"))
                    bkt.put("jamtanggal_feedback",jsonObject.getString("jamtanggal_feedback"))
                    bkt.put("profil",jsonObject.getString("profil"))

                    dataFeedback.add(bkt)
                }
                feedbackAdp.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode) {
                    "show_data_feedback" -> {
                        hm.put("mode", "show_data_feedback")
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
            Method.POST,urlClass.urlFeedback,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    txPesanFeedback.setText("")
                    restartActivity()
                    Toast.makeText(parent,"Berhasil menghapus Pesan!", Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "delete" -> {
                        hm.put("mode", "delete")
                        hm.put("kd_feedback", idF)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun kirimFeedback(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlFeedback,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    txPesanFeedback.setText("")
                    restartActivity()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode) {
                    "insert" -> {
                        hm.put("mode", "insert")
                        hm.put("teks_feedback", teks)
                    }
                    "insert_reply" -> {
                        hm.put("mode", "insert_reply")
                        hm.put("teks_feedback", teks)
                        hm.put("nama_reply", namaReplyMain.text.toString())
                        hm.put("teks_reply", teksReplyMain.text.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}