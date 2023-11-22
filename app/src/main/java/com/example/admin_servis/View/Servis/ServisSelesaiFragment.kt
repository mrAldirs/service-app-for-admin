package com.example.admin_servis.View.Servis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterServisRiwayat
import com.example.admin_servis.ViewModel.ServisViewModel
import com.example.admin_servis.databinding.ServisStatusFragmentBinding

class ServisSelesaiFragment : Fragment() {
    private lateinit var binding: ServisStatusFragmentBinding
    private lateinit var servisViewModel: ServisViewModel
    lateinit var adapter: AdapterServisRiwayat
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ServisStatusFragmentBinding.inflate(layoutInflater)
        v = binding.root

        servisViewModel = ViewModelProvider(this).get(ServisViewModel::class.java)

        adapter = AdapterServisRiwayat(ArrayList())
        binding.recyclerView.layoutManager = LinearLayoutManager(v.context)
        binding.recyclerView.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    private fun loadData(nama: String) {
        servisViewModel.loadData("riwayat_selesai" ,nama)
        servisViewModel.servisData.observe(viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })

        servisViewModel.isEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if (isEmpty) {
                // Data is empty, show the emptyTextView and hide the RecyclerView
                binding.tvEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                // Data is available, hide the emptyTextView and show the RecyclerView
                binding.tvEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
    }
}