package com.example.admin_servis.Profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.admin_servis.R
import com.example.admin_servis.databinding.ProfilEditFragmentBinding

class FragmentPasswordEdit : DialogFragment() {
    private lateinit var b: ProfilEditFragmentBinding
    lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilEditFragmentBinding.inflate(layoutInflater)
        v = b.root

        b.etUsernameProfil.isEnabled = false
        b.etPasswordProfil.isEnabled = false

        b.etUsernameProfil.setText("admin")
        b.etPasswordProfil.setText("admin")

        return v
    }
}