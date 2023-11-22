package com.example.admin_servis.View.Servis

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Helper.CurrencyHelper
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.R
import com.example.admin_servis.Servis.ServisBayarFragment
import com.example.admin_servis.UrlClass
import com.example.admin_servis.View.Chat.ChatActivity
import com.example.admin_servis.ViewModel.MekanikViewModel
import com.example.admin_servis.ViewModel.ServisViewModel
import com.example.admin_servis.databinding.ServisDetailActivityBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ServisDetailActivity : AppCompatActivity() {

    lateinit var binding: ServisDetailActivityBinding
    lateinit var urlClass : UrlClass
    private lateinit var mekanikViewModel: MekanikViewModel
    private lateinit var servisViewModel: ServisViewModel
    var kdT = ""
    var kdS = ""
    var kdU = ""
    var sts = ""
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServisDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        urlClass = UrlClass()
        mekanikViewModel = ViewModelProvider(this).get(MekanikViewModel::class.java)
        servisViewModel = ViewModelProvider(this).get(ServisViewModel::class.java)

        var paket : Bundle? = intent.extras
        binding.detailServisIdServis.setText("Kode : "+paket?.getString("kode").toString())

        binding.btnKonfirmaiServis.setOnClickListener {
            val frag = ServisKonfirmasiFragment()

            frag.show(supportFragmentManager, "ServisKonfirmasi")
        }

        binding.btnKonfirmaiBayarServis.setOnClickListener {
            val frag = ServisBayarFragment()

            frag.show(supportFragmentManager, "ServisKonfirmasi")
        }

        binding.imgDetailServis.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }

        binding.btnBatalkan.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin membatalkan servis ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    batalkan()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        detailServis("detail_servis")
    }

    private fun detailServis(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val kdTran = jsonObject.getString("kd_transaksi")
                val tglTran = jsonObject.getString("tgl_transaksi")
                val nama = jsonObject.getString("nama")
                val alamat = jsonObject.getString("alamat")
                val email = jsonObject.getString("email")
                val noHp = jsonObject.getString("no_hp")
                val kerusakan = jsonObject.getString("kerusakan")
                val kdServis = jsonObject.getString("kd_servis")
                val namaBarang = jsonObject.getString("nama_barang")
                val jenisBarang = jsonObject.getString("jenis_barang")
                val tglServis = jsonObject.getString("tgl_servis")
                val catatanServis = jsonObject.getString("catatan_transaksi")
                val img = jsonObject.getString("img_barang")
                val status = jsonObject.getString("status_transaksi")
                val kdUser = jsonObject.getString("kd_user")

                kdU = kdUser
                kdT = kdTran
                kdS = kdServis
                sts = status
                imgUrl = img
                binding.detailServisTglTransaksi.setText(tglTran)
                binding.detailServisNama.setText(nama)
                binding.detailServisAlamat.setText(alamat)
                binding.detailServisEmail.setText(email)
                binding.detailServisNoHp.setText(noHp)
                binding.detailServisKerusakan.setText(kerusakan)
                binding.detailServisNamaBarang.setText(namaBarang)
                binding.detailServisJenisBarang.setText(jenisBarang)
                Picasso.get().load(img).into(binding.imgDetailServis)

                binding.btnChat.setOnClickListener {
                    val intent = Intent(this, ChatActivity::class.java)
                    intent.putExtra("kode", kdTran)
                    intent.putExtra("topik", "Servis")
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

                if (status.toString().equals("Belum")) {
                    binding.detailServisStsTransaksi.setText("Baru")
                    binding.detailServisStsTransaksi.setTextColor(Color.parseColor("#2046FF"))
                    binding.btnKonfirmaiBayarServis.visibility = View.GONE
                    binding.btnBatalkan.visibility = View.GONE
                    binding.cdMekanik.visibility = View.GONE
                } else if (status.toString().equals("Proses")){
                    detailMekanik(kdServis)
                    binding.detailServisStsTransaksi.setText("Proses")
                    binding.detailServisStsTransaksi.setTextColor(Color.parseColor("#000000"))
                    binding.btnKonfirmaiBayarServis.visibility = View.VISIBLE
                    binding.btnKonfirmaiServis.visibility = View.GONE
                    binding.btnBatalkan.visibility = View.VISIBLE
                } else if (status.toString().equals("Tolak")) {
                    binding.detailServisStsTransaksi.setText("Ditolak")
                    binding.detailServisStsTransaksi.setTextColor(Color.RED)
                    binding.btnKonfirmaiBayarServis.visibility = View.GONE
                    binding.cdMekanik.visibility = View.GONE
                    binding.btnBatalkan.visibility = View.GONE
                } else {
                    detailMekanik(kdServis)
                    binding.detailServisStsTransaksi.setText("Selesai")
                    binding.detailServisStsTransaksi.setTextColor(Color.parseColor("#17ad96"))
                    binding.btnKonfirmaiServis.visibility = View.GONE
                    binding.btnKonfirmaiBayarServis.visibility = View.GONE
                    binding.btnBatalkan.visibility = View.GONE
                }

                if (!tglServis.equals("0000-00-00 00:00:00") && status.equals("Selesai")) {
                    binding.cardPembayranDetailServis.visibility = View.VISIBLE
                    detailPembayaran("detail_pembayaran_servis")
                }

                if (tglServis.equals("null") && catatanServis.equals("null")) {
                    binding.detailServisTglServis.setText("belum ditentukan")
                    binding.detailServisCatatanServis.setText("*")
                } else {
                    binding.detailServisTglServis.setText(tglServis)
                    binding.detailServisCatatanServis.setText(catatanServis)
                }

                if (tglServis.equals("0000-00-00 00:00:00")) {
                    binding.detailServisTglServis.setText("Anda telah menolak servis")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode) {
                    "detail_servis" -> {
                        hm.put("mode", "detail_servis")
                        hm.put("kd_transaksi", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun batalkan() {
        servisViewModel.batalkan(kdT).observe(this, Observer {
            Toast.makeText(this, "Berhasil membatalkan Servis!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        })
    }

    private fun detailMekanik(kode: String) {
        mekanikViewModel.detail(kode).observe(this, Observer { detail ->
            binding.detailNamaMekanik.setText(detail.nama_mekanik)
            Picasso.get().load(detail.foto_mekanik).into(binding.detailFotoMekanik)
            binding.ratingBar.rating = detail.average_mekanik.toFloat()
            binding.tvRating.setText(detail.average_mekanik)
        })
    }

    private fun detailPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val statusBayar = jsonObject.getString("status_pembayaran")
                val tglP = jsonObject.getString("tgl_pembayaran")
                val tot = jsonObject.getString("total_bayar").toInt()

                binding.detailServisStatusPembayaran.setText(statusBayar)
                binding.detailServisTglBayar.setText(tglP)
                binding.detailServisTotalPembayaran.text = CurrencyHelper.formatCurrency(tot)

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "detail_pembayaran_servis" -> {
                        hm.put("mode", "detail_pembayaran_servis")
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