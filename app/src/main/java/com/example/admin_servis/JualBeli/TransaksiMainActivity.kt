package com.example.admin_servis.JualBeli

import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.admin_servis.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.transaksi_main_activity.*

class TransaksiMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaksi_main_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        val viewPager = findViewById<ViewPager>(R.id.viewPagerTransaksi)
        viewPager.adapter = PageBarangAdapter(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayoutTransaksi)
        tabLayout.setupWithViewPager(viewPager)

        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        jdLayout.startAnimation(slide_start)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    class PageBarangAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            when(position) {
                0 -> {
                    return FragmentTransaksiJual()
                }
                1 -> {
                    return FragmentTransaksiBeli()
                }
                else -> {
                    return FragmentTransaksiJual()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position) {
                0 -> {
                    return "Penjualan"
                }
                1 -> {
                    return "Pembelian"
                }
            }
            return super.getPageTitle(position)
        }

    }
}