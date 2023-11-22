package com.example.admin_servis.PageAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.admin_servis.View.Servis.ServisDitolakFragment
import com.example.admin_servis.View.Servis.ServisSelesaiFragment

class ServisPage (fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ServisSelesaiFragment()
            }
            1 -> {
                return ServisDitolakFragment()
            }
            else -> {
                return ServisSelesaiFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Selesai"
            }
            1 -> {
                return "Ditolak"
            }
        }
        return super.getPageTitle(position)
    }

}