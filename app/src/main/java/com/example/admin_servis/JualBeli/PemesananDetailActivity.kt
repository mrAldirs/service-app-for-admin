package com.example.admin_servis.JualBeli

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.View.Chat.ChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pemesanan_detail_activity.*
import org.json.JSONObject

class PemesananDetailActivity : AppCompatActivity() {

    lateinit var urlClass : UrlClass
    var kdT = ""
    var kdB = ""
    var kdU = ""
    var kdJ = ""
    var pay = ""
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pemesanan_detail_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        detailOrderKodeTransaksi.setText("Kode : "+paket?.getString("kode").toString())

        btnKonfirmaiOrder.setOnClickListener {
            var frag = PemesananKonfirmasiFragment()
            var paket : Bundle? = intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            frag.arguments = bundle

            frag.show(supportFragmentManager, "PemesananKonfirmasiFragment")
        }
    }

    override fun onStart() {
        super.onStart()
        detailOrder("detail_order")
    }

    fun detailOrder(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_transaksi")
                val st2 = jsonObject.getString("tgl_transaksi")
                val st3 = jsonObject.getString("nama")
                val st4 = jsonObject.getString("alamat")
                val st5 = jsonObject.getString("email")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("nama_barang")
                val st8 = jsonObject.getString("jenis_barang")
                val st9 = jsonObject.getString("status_transaksi")
                val st10 = jsonObject.getString("ket_barang")
                val st11 = jsonObject.getString("catatan_transaksi")
                val st12 = jsonObject.getString("image")
                val st13 = jsonObject.getString("kd_user")
                val st14 = jsonObject.getString("kd_barang")

                kdU = st13
                kdT = st1
                kdB = st14
                imgUrl = st12
                detailOrderTglTransaksi.setText(st2)
                detailOrderNama.setText(st3)
                detailOrderAlamat.setText(st4)
                detailOrderEmail.setText(st5)
                detailOrderNoHp.setText(st6)
                detailOrderNamaBarang.setText(st7)
                detailOrderJenisBarang.setText(st8)
                detailOrderKeterangan.setText(st10)

                btn_chat.setOnClickListener {
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("kode", st1)
                    intent.putExtra("topik", "Pre-order")
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

                if (st9.toString().equals("Belum")){
                    detailOrderStatus.setText("Baru")
                    detailOrderStatus.setTextColor(Color.parseColor("#2046FF"))
                } else {
                    detailOrderStatus.setText("Selesai")
                    detailOrderStatus.setTextColor(Color.parseColor("#17ad96"))
                    btnKonfirmaiOrder.visibility = View.GONE
                }

                if (st11.toString().equals("null")) {
                    detailOrderCatatan.setText("*")
                } else {
                    detailOrderCatatan.setText(st11)
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode) {
                    "detail_order" -> {
                        hm.put("mode", "detail_order")
                        hm.put("kd_transaksi", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}