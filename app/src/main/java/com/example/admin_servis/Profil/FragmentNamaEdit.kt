package com.example.admin_servis.Profil

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.admin_servis.R
import com.example.admin_servis.databinding.ProfilEditFragmentBinding
import kotlinx.android.synthetic.main.profil_edit_fragment.view.*

class FragmentNamaEdit : DialogFragment() {
    private lateinit var b: ProfilEditFragmentBinding
    lateinit var v : View
    var kode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilEditFragmentBinding.inflate(layoutInflater)
        v = b.root

        kode = arguments?.getString("kode").toString()
        if (kode.equals("nama")) {
            b.judul.setText("Profil Toko")
            b.input1.hint = "Nama"
            b.input2.hint = "Nomor Handphone"
            b.etUsernameProfil.setText("Rukin Servis Elektronik")
            b.etPasswordProfil.setText("081249710599")
            b.etPasswordProfil.inputType = InputType.TYPE_CLASS_PHONE
        } else if (kode.equals("alamat")) {
            b.judul.setText("Alamat")
            b.input1.hint = "Alamat"
            b.etUsernameProfil.setSingleLine(false)
            b.etUsernameProfil.setLines(3)
            b.etUsernameProfil.gravity = Gravity.TOP
            b.etUsernameProfil.setText("Jl. Wijaya Kusuma, RT11 RW02, Jajar, Wates, Kediri, Jawa Timur")
            b.input2.visibility = View.GONE
        } else {
            b.judul.setText("Email")
            b.input1.hint = "Email"
            b.etUsernameProfil.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            b.etUsernameProfil.setText("rukinserviselektronik@gmail.com")
            b.input2.visibility = View.GONE
        }

        b.etUsernameProfil.isEnabled = false
        b.etPasswordProfil.isEnabled = false

        return v
    }
}