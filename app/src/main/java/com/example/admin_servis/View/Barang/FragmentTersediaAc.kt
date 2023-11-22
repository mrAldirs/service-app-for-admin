package com.example.admin_servis.View.Barang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterKatalog
import com.example.admin_servis.ViewModel.BarangViewModel
import com.example.admin_servis.databinding.BrgtersediaJenisFragmentBinding

class FragmentTersediaAc : Fragment() {
    private lateinit var b: BrgtersediaJenisFragmentBinding
    private lateinit var bVM: BarangViewModel
    lateinit var thisParent : TersediaMainActivity
    lateinit var v : View
    lateinit var adapter: AdapterKatalog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = BrgtersediaJenisFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as TersediaMainActivity

        bVM = ViewModelProvider(this).get(BarangViewModel::class.java)

        adapter = AdapterKatalog(ArrayList(), thisParent)
        b.rvBrgTersedia.layoutManager = LinearLayoutManager(v.context)
        b.rvBrgTersedia.adapter = adapter

        b.btnSearchBrgTersedia.setOnClickListener {
            loadData("read_ac_tersedia", b.textSearchBrgTersedia.text.toString().trim())
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData("read_ac_tersedia", "")
    }

    fun loadData(mode: String, nama: String) {
        bVM.loadData(mode, nama).observe(requireActivity(), Observer { list ->
            adapter.setData(list)
        })
    }
}