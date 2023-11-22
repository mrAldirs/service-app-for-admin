package com.example.admin_servis

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterDetailMain
import com.example.admin_servis.ViewModel.LaporanViewModel
import com.example.admin_servis.databinding.MainDetailActivityBinding

class DetailMainActivity : AppCompatActivity() {
    private lateinit var b: MainDetailActivityBinding
    private lateinit var transactionViewModel: LaporanViewModel
    private lateinit var adapter: AdapterDetailMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MainDetailActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        transactionViewModel = ViewModelProvider(this).get(LaporanViewModel::class.java)

        adapter = AdapterDetailMain(ArrayList())
        b.recyclerView.layoutManager = LinearLayoutManager(this)
        b.recyclerView.adapter = adapter

        transactionViewModel.getTransactionData().observe(this, Observer { transactionData ->
            adapter.setData(transactionData)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}