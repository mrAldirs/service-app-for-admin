package com.example.admin_servis.View.Barang

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
import com.example.admin_servis.Helper.CurrencyHelper
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.JualBeli.JualDetailActivity
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.BarangViewModel
import com.example.admin_servis.databinding.BrgDetailActivityBinding
import com.squareup.picasso.Picasso

class BarangDetailActivity : AppCompatActivity() {
    private lateinit var b: BrgDetailActivityBinding
    private lateinit var bVM: BarangViewModel
    var kdBrg = ""
    var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = BrgDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        bVM = ViewModelProvider(this).get(BarangViewModel::class.java)

        var paket : Bundle? = intent.extras
        b.detailKatalogKd.setText("Kode "+paket?.getString("kdB").toString())

        b.imgDetailBarang.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }

        b.btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin menghapus data barang dari katalog?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    delete()
                    onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        b.btnEdit.setOnClickListener {
            onEditButtonClick()
        }
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    fun detail() {
        var paket : Bundle? = intent.extras
        bVM.detail(paket?.getString("kdB").toString()).observe(this, Observer { barang ->
            b.detailKatalogKd.setText("Kode : "+barang.kd_barang)
            kdBrg = barang.kd_barang
            b.detailKatalogTglUpload.setText(barang.tgl_upload)
            b.detailKatalogNama.setText(barang.nama_barang)
            b.detailKatalogJenis.setText(barang.jenis_barang)
            b.detailKatalogHarga.text = CurrencyHelper.formatCurrency(barang.harga_katalog.toInt())
            b.detailKatalogSpesifikasi.setText(barang.spesifikasi)

            if (barang.status_katalog.equals("Ready")) {
                b.detailKatalogStatus.setText("Barang Tersedia")
                b.detailKatalogStatus.setTextColor(Color.parseColor("#2046FF"))
                b.btnDetailTransaksi.visibility = View.GONE
            } else if (barang.status_katalog.equals("Pending")) {
                b.detailKatalogStatus.setText("Transaksi Pending")
                b.detailKatalogStatus.setTextColor(Color.parseColor("#EC0D0D"))
                b.btnEdit.visibility = View.GONE
            } else {
                b.detailKatalogStatus.setText("Barang Telah Terjual")
                b.detailKatalogStatus.setTextColor(Color.parseColor("#17ad96"))
                b.btnEdit.visibility = View.GONE
                var kdT = paket?.getString("kdT").toString()
                b.btnDetailTransaksi.setOnClickListener {
                    val intent = Intent(this, JualDetailActivity::class.java)
                    intent.putExtra("kode", kdT)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
            }

            imgUrl = barang.img_barang

            if (barang.ket_barang.equals("null")) {
                b.detailKatalogKeterangan.setText("*")
            } else {
                b.detailKatalogKeterangan.setText("*"+barang.ket_barang)
            }
            Picasso.get().load(barang.img_barang).into(b.imgDetailBarang)
        })
    }

    fun delete() {
        var paket : Bundle? = intent.extras
        bVM.delete(paket?.getString("kdB").toString()).observe(this, Observer { result ->
            Toast.makeText(this, "Berhasil menghapus data barang dari katalog!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun openTersediaEditFragment(id: String) {
        val fragment = FragmentTersediaEdit.newInstance(id)
        fragment.show(supportFragmentManager, "FragmentTersediaEdit")
    }

    private fun onEditButtonClick() {
        var paket : Bundle? = intent.extras
        openTersediaEditFragment(paket?.getString("kdB").toString())
    }
}