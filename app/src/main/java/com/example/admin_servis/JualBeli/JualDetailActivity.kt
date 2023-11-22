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
import com.example.admin_servis.Helper.CurrencyHelper
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.jual_detail_activity.*
import org.json.JSONObject

class JualDetailActivity : AppCompatActivity() {
    lateinit var urlClass : UrlClass

    var kdU = ""
    var kdT = ""
    var kdP = ""
    var kdB = ""
    var imgUrl = ""
    var pay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jual_detail_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        detailJualKodeTransaksi.setText("Kode : "+paket?.getString("kode").toString())

        detailJualImage.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }

        jualBtnKonfirmasiPengiriman.setOnClickListener {
            var frag = JualPengirimanFragment()
            var paket : Bundle? = intent.extras
            var kode = paket?.getString("kode")
            var py = paket?.getString("pay")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            bundle.putString("pay", py)
            bundle.putString("pay", pay)
            frag.arguments = bundle

            frag.show(supportFragmentManager, "JualPengirimanFragment")
        }

        detail("detail_jual")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun detail(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_transaksi")
                val st2 = jsonObject.getString("kd_user")
                val st3 = jsonObject.getString("nama")
                val st4 = jsonObject.getString("alamat")
                val st5 = jsonObject.getString("email")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("nama_barang")
                val st8 = jsonObject.getString("jenis_barang")
                val st9 = jsonObject.getString("tgl_transaksi")
                val st10 = jsonObject.getString("catatan_transaksi")
                val st11 = jsonObject.getString("status_transaksi")
                val st13 = jsonObject.getString("image")
                val st14 = jsonObject.getString("status_pembayaran")
                val st15 = jsonObject.getString("tgl_pembayaran")
                val st16 = jsonObject.getString("total_bayar").toInt()
                val st17 = jsonObject.getString("rek_payment")
                val st18 = jsonObject.getString("kd_pembayaran")
                val st19 = jsonObject.getString("kd_barang")

                imgUrl = st13
                kdT = st1
                kdU = st2
                kdP = st18
                kdB = st19
                pay = st17

                if (st11.equals("Belum")) {
                    detailJualStatusTransaksi.setText("Belum Bayar")
                    detailJualStatusTransaksi.setTextColor(Color.RED)
                    jualBtnKonfirmasiPengiriman.visibility = View.GONE
                } else if(st11.equals("Proses")) {
                    detailJualStatusTransaksi.setText("Proses")
                    detailJualStatusTransaksi.setTextColor(Color.parseColor("#2046FF"))
                } else {
                    detailJualStatusTransaksi.setTextColor(Color.parseColor("#17ad96"))
                    detailJualStatusTransaksi.setText("selesai")
                    jualBtnKonfirmasiPengiriman.visibility = View.GONE
                }

                if (st14.equals("Proses")) {
                    detailJualStatusBayar.setTextColor(Color.RED)
                    detailJualStatusBayar.setText("Pending")
                } else {
                    detailJualStatusBayar.setTextColor(Color.parseColor("#17ad96"))
                    detailJualStatusBayar.setText(st14)
                }

                if (st15.equals("null")) {
                    detailJualTglPembayaran.setText("Belum melakukan pembayaran")
                } else {
                    detailJualTglPembayaran.setText(st15)
                }

                if (st10.equals("") || st10.equals("null")) {
                    detailJualCatatan.setText("*")
                } else {
                    detailJualCatatan.setText("*"+st10)
                }

                detailJualNama.setText(st3)
                detailJualAlamat.setText(st4)
                detailJualEmail.setText(st5)
                detailJualNoHp.setText(st6)
                detailJualNamaBarang.setText(st7)
                detailJualJenisBarang.setText(st8)
                detailJualTglTransaksi.setText(st9)
                detailJualTotalBayar.text = CurrencyHelper.formatCurrency(st16)
                Picasso.get().load(st13).into(detailJualImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode) {
                    "detail_jual" -> {
                        hm.put("mode", "detail_jual")
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