package com.example.admin_servis.Informasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin_servis.databinding.InformasiDetailFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentInformasiDetail : BottomSheetDialogFragment() {
    private lateinit var b: InformasiDetailFragmentBinding
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = InformasiDetailFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnBatalkan.setOnClickListener { dismiss() }

        b.btnTutup.setOnClickListener { dismiss() }

        val bundle = arguments
        b.detailTanggal.setText(bundle?.get("tanggal").toString())
        b.detailTeksInformasi.setText(bundle?.get("teks").toString())

        return v
    }
}