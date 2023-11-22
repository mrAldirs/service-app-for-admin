package com.example.admin_servis.View.User

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterUser
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.UserViewModel
import com.example.admin_servis.databinding.UserMainActivityBinding

class UserMainActivity : AppCompatActivity() {
    private lateinit var b: UserMainActivityBinding
    private lateinit var uVM: UserViewModel
    lateinit var adapter: AdapterUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = UserMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        uVM = ViewModelProvider(this).get(UserViewModel::class.java)

        adapter = AdapterUser(ArrayList(), this)
        b.rvUser.layoutManager = LinearLayoutManager(this)
        b.rvUser.adapter = adapter

        b.btnCari.setOnClickListener {
           loadData(b.txCari.text.toString().trim())
        }

        b.btnInsertAkun.setOnClickListener {
            val frag = FragmentAkunInsert()

            frag.show(supportFragmentManager, "FragmentAkunInsert")
        }
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun loadData(nama: String) {
        uVM.loadData(nama).observe(this, Observer { list ->
            adapter.setData(list)
        })
    }
}