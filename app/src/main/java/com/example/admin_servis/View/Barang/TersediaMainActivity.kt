package com.example.admin_servis.View.Barang

import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.BarangViewModel
import com.example.admin_servis.databinding.BrgtersediaMainActivityBinding
import com.google.android.material.tabs.TabLayout

class TersediaMainActivity : AppCompatActivity() {
    private lateinit var b: BrgtersediaMainActivityBinding
    private lateinit var bVM: BarangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = BrgtersediaMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        bVM = ViewModelProvider(this).get(BarangViewModel::class.java)

        val viewPager = findViewById<ViewPager>(R.id.viewPagerBarang)
        viewPager.adapter = PageBarangAdapter(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayoutBarang)
        tabLayout.setupWithViewPager(viewPager)

        val slide_start = AnimationUtils.loadAnimation(this, R.anim.translate_start)
        b.jdLayout.startAnimation(slide_start)

        b.btnInsertBarang.setOnClickListener {
            val frag = FragmentTersediaInsert()

            frag.show(supportFragmentManager, "FragmentTersediaInsert")
        }
    }

    fun restartActivity() {
        recreate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun delete(kd: String) {
        bVM.delete(kd).observe(this, Observer { result ->
            recreate()
            Toast.makeText(this, "Berhasil menghapus barang di katalog!", Toast.LENGTH_SHORT).show()
        })
    }

    class PageBarangAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 5
        }

        override fun getItem(position: Int): Fragment {
            when(position) {
                0 -> {
                    return FragmentTersediaSemua()
                }
                1 -> {
                    return FragmentTersediaTelevisi()
                }
                2 -> {
                    return FragmentTersediaAc()
                }
                3 -> {
                    return FragmentTersediaHp()
                }
                4 -> {
                    return FragmentTersediaLaptop()
                }
                else -> {
                    return FragmentTersediaSemua()
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position) {
                0 -> {
                    return "Semua"
                }
                1 -> {
                    return "TV"
                }
                2 -> {
                    return "AC"
                }
                3 -> {
                    return "Hp"
                }
                4 -> {
                    return "Laptop"
                }
            }
            return super.getPageTitle(position)
        }

    }
}