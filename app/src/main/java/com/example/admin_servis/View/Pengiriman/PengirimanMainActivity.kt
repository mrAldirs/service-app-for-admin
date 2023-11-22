package com.example.admin_servis.View.Pengiriman

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterPengiriman
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.PengirimanViewModel
import com.example.admin_servis.databinding.PengirimanMainActivityBinding

class PengirimanMainActivity : AppCompatActivity() {
    private lateinit var b: PengirimanMainActivityBinding
    private lateinit var pVM: PengirimanViewModel
    private lateinit var adapter: AdapterPengiriman

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = PengirimanMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        pVM = ViewModelProvider(this).get(PengirimanViewModel::class.java)
        adapter = AdapterPengiriman(ArrayList())
        b.recyclerView.layoutManager = LinearLayoutManager(this)
        b.recyclerView.adapter = adapter

        b.btnSearch .setOnClickListener {
            loadData("load_data", b.textSearch.text.toString().trim())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        loadData("load_data", "")
    }

    private fun loadData(mode: String, nama: String) {
        pVM.loadData(mode, nama).observe(this, Observer { list ->
            adapter.setData(list)
        })
    }
}