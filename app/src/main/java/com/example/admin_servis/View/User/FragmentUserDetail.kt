package com.example.admin_servis.View.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.UserViewModel
import com.example.admin_servis.databinding.UserEditFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class FragmentUserDetail : BottomSheetDialogFragment() {
    private lateinit var b: UserEditFragmentBinding
    private lateinit var uVM: UserViewModel
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = UserEditFragmentBinding.inflate(layoutInflater)
        v = b.root

        uVM = ViewModelProvider(this).get(UserViewModel::class.java)

        b.btnBatalkan.setOnClickListener {
            dismiss()
        }

        b.btnTutup.setOnClickListener {
            dismiss()
        }

        val bundle = arguments
        b.tvKdEditUser.text = bundle?.get("kode").toString()

        b.judul.setText("Pelanggan Detail")
        b.txNamaEditUser.isEnabled = false
        b.txAlamatEditUser.isEnabled = false
        b.txEmailEditUser.isEnabled = false
        b.txUsiaEditUser.isEnabled = false
        b.txNoHpEditUser.isEnabled = false
        b.btnSimpanEditUser.visibility = View.GONE

        return v
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    private fun detail() {
        uVM.detail(arguments?.get("kode").toString()).observe(this, Observer { detail ->
            b.tvKdEditUser.setText(detail.userModel.kd_user)
            b.txNamaEditUser.setText(detail.userModel.nama)

            if (!detail.userModel.alamat.equals("null") || !detail.userModel.email.equals("null") || !detail.userModel.usia.equals("null") ||
                !detail.userModel.no_hp.equals("null")) {
                b.txAlamatEditUser.setText(detail.userModel.alamat)
                b.txEmailEditUser.setText(detail.userModel.email)
                b.txUsiaEditUser.setText(detail.userModel.usia)
                b.txNoHpEditUser.setText(detail.userModel.no_hp)
            } else {
                b.txAlamatEditUser.setText(detail.userModel.alamat)
                b.txEmailEditUser.setText(detail.userModel.email)
                b.txUsiaEditUser.setText(detail.userModel.usia)
                b.txNoHpEditUser.setText(detail.userModel.no_hp)
            }

            if (detail.userModel.profil.equals("null")) {
                Picasso.get().load(R.drawable.akun).into(b.imgEditUser)
            } else {
                Picasso.get().load(detail.userModel.profil).into(b.imgEditUser)
            }

            b.txUsername.setText(detail.akunModel.username)
            b.txPassoword.setText(detail.akunModel.password)
        })
    }
}