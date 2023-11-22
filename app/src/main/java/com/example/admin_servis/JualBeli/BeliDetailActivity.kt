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
import com.example.admin_servis.View.Chat.ChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.beli_detail_activity.*
import org.json.JSONObject

class BeliDetailActivity : AppCompatActivity() {
    lateinit var urlClass : UrlClass

    var kdT = ""
    var kdB = ""
    var kdU = ""
    var kdP = ""
    var hrg : Int = 0
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beli_detail_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        detailBeliKodeTransaksi.setText("Kode : "+paket?.getString("kode").toString())

        detailBeliImage.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }

        beliBtnKonfirmasi.setOnClickListener {
            var frag = BeliKonfirmasiFragment()
            var paket : Bundle? = intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            bundle.putString("pay", detailBeliPayment.text.toString())
            frag.arguments = bundle

            frag.show(supportFragmentManager, "BeliKonfirmasiFragment")
        }

        beliBtnKonfirmasiBayar.setOnClickListener {
            var frag = BeliPembayaranFragment()
            var paket : Bundle? = intent.extras
            var kode = paket?.getString("kode")
            var harga = paket?.getString("hrg")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", kdT)
            bundle.putString("hrg", harga)
            bundle.putString("hrg", hrg.toString())
            frag.arguments = bundle

            frag.show(supportFragmentManager, "BeliKonfirmasiFragment")
        }
    }

    override fun onStart() {
        super.onStart()
        detailBeli("detail_beli")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun detailBeli(mode: String) {
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
                val st9 = jsonObject.getString("total_bayar").toInt()
                val st10 = jsonObject.getString("rek_payment")
                val st11 = jsonObject.getString("status_transaksi")
                val st12 = jsonObject.getString("ket_barang")
                val st13 = jsonObject.getString("image")
                val st14 = jsonObject.getString("kd_user")
                val st15 = jsonObject.getString("kd_barang")

                btn_chat.setOnClickListener {
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("kode", st1)
                    intent.putExtra("topik", "Tawar Menawar")
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

                hrg = st9
                kdB = st15
                kdU = st14
                kdT = st1
                imgUrl = st13
                detailBeliPayment.setText(st10)
                detailBeliTglUpload.setText(st2)
                detailBeliNama.setText(st3)
                detailBeliAlamat.setText(st4)
                detailBeliEmail.setText(st5)
                detailBeliNoHp.setText(st6)
                detailBeliNamaBarang.setText(st7)
                detailBeliJenisBarang.setText(st8)
                detailBeliHargaKatalog.text = CurrencyHelper.formatCurrency(st9)
                detailBeliKeterangan.setText(st12)
                Picasso.get().load(st13).into(detailBeliImage)

                if (st11.toString().equals("Belum")) {
                    detailBeliStatusTransaksi.setText("Belum dikonfirmasi")
                    detailBeliStatusTransaksi.setTextColor(Color.parseColor("#2046FF"))
                    cardPembayaran.visibility = View.GONE
                } else if (st11.toString().equals("Proses")) {
                    detailBeliStatusTransaksi.setText("Proses Pembelian")
                    detailBeliStatusTransaksi.setTextColor(Color.parseColor("#2046FF"))
                    detailPembayaran("detail_pembayaran_beli")
                    cardPembayaran.visibility = View.VISIBLE
                } else if (st11.toString().equals("Tolak")) {
                    detailBeliStatusTransaksi.setText("Membatalkan Pembelian")
                    detailBeliStatusTransaksi.setTextColor(Color.RED)
                    cardPembayaran.visibility = View.VISIBLE
                    detailBeliCatatan.setText("*")
                    detailBeliStatusBayar.setText("Membatalkan Pembelian")
                    detailBeliStatusBayar.setTextColor(Color.RED)
                } else {
                    detailBeliStatusTransaksi.setText(st11)
                    detailBeliStatusTransaksi.setTextColor(Color.parseColor("#17ad96"))
                    cardPembayaran.visibility = View.VISIBLE
                    detailPembayaran("detail_pembayaran_beli")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode) {
                    "detail_beli" -> {
                        hm.put("mode", "detail_beli")
                        hm.put("kd_transaksi", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun detailPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st14 = jsonObject.getString("status_pembayaran")
                val st15 = jsonObject.getString("catatan_transaksi")
                val st16 = jsonObject.getString("kd_pembayaran")
                val st17 = jsonObject.getString("tgl_transaksi")

                if (st17.toString().equals("null")) {
                    detailBeliTglTransaksi.setText("belum divalidasi")
                    detailBeliTglTransaksi.setTextColor(Color.RED)
                } else if (st17.toString().equals("0000-00-00 00:00:00")) {
                    detailBeliTglTransaksi.setText("belum divalidasi")
                    detailBeliTglTransaksi.setTextColor(Color.RED)
                } else {
                    detailBeliTglTransaksi.setText(st17)
                    detailBeliStatusTransaksi.setTextColor(Color.parseColor("#17ad96"))
                }

                kdP = st16
                detailBeliCatatan.setText(st15)
                if (st14.toString().equals("Belum")) {
                    detailBeliStatusBayar.setText("Belum Bayar")
                    detailBeliStatusBayar.setTextColor(Color.RED)
                    beliBtnKonfirmasi.visibility = View.GONE
                    beliBtnKonfirmasiBayar.visibility = View.VISIBLE
                } else if (st14.toString().equals("Selesai")) {
                    beliBtnKonfirmasi.visibility = View.GONE
                    beliBtnKonfirmasiBayar.visibility = View.GONE
                    detailBeliStatusBayar.setText("Sudah Bayar")
                    detailBeliStatusBayar.setTextColor(Color.parseColor("#17ad96"))
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "detail_pembayaran_beli" -> {
                        hm.put("mode", "detail_pembayaran_beli")
                        hm.put("kd_transaksi", kdT)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}