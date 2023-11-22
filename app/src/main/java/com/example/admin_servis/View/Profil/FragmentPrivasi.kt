package com.example.admin_servis.View.Profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin_servis.databinding.ProfilPrivasiFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentPrivasi : BottomSheetDialogFragment() {
    private lateinit var b: ProfilPrivasiFragmentBinding
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilPrivasiFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.btnBatalkan.setOnClickListener { dismiss() }
        b.btnTutup.setOnClickListener { dismiss() }

        return v
    }
}