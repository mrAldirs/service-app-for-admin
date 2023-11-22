package com.example.admin_servis.View.Laporan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterPendapatan
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.LaporanViewModel
import com.example.admin_servis.databinding.LaporanDetailFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentLaporanDetail : BottomSheetDialogFragment() {
    private lateinit var b: LaporanDetailFragmentBinding
    private lateinit var lapVM: LaporanViewModel
    lateinit var v: View
    private lateinit var adapter: AdapterPendapatan
    val sortSp = arrayOf("--Filter--","2023","2022","2021","2020","2019","2018","2017","2016","2015","2014","2013","2012","2011","2010")
    lateinit var adapterSort: ArrayAdapter<String>
    var nilaiSort = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = LaporanDetailFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnTutup.setOnClickListener { dismiss() }
        b.btnBatalkan.setOnClickListener { dismiss() }

        lapVM = ViewModelProvider(this).get(LaporanViewModel::class.java)
        adapter = AdapterPendapatan(ArrayList(), this)
        b.recyclerView.layoutManager = LinearLayoutManager(v.context)
        b.recyclerView.adapter = adapter

        adapterSort = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,sortSp)
        b.spSorting.adapter = adapterSort
        b.spSorting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position)

                if (selectedItem.equals("--Filter--")) {
                    loadData("")
                    nilaiSort = "2023"
                } else {
                    loadData(selectedItem.toString())
                    nilaiSort = selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    private fun loadData(thn: String) {
        lapVM.detailPendapatan(thn).observe(requireActivity(), Observer { list ->
            adapter.setData(list)
        })
    }
}