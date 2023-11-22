package com.example.admin_servis.Profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.admin_servis.R
import kotlinx.android.synthetic.main.pin_fragment.view.*

class FragmentPinEdit : DialogFragment() {
    lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.pin_fragment, container, false)

        v.btnBatalkan.visibility = View.GONE
        v.btnTutup.visibility = View.GONE
        v.judul.setText("PIN Baru")
        
        return v
    }
}