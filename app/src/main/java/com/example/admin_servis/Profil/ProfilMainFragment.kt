package com.example.admin_servis.Profil

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.*
import com.example.admin_servis.View.Profil.FragmentPrivasi
import com.example.admin_servis.databinding.ProfilMainFragmentBinding
import org.json.JSONObject

class ProfilMainFragment : Fragment() {
    private lateinit var b: ProfilMainFragmentBinding
    lateinit var thisParent: MainActivity
    lateinit var v : View

    lateinit var urlClass: UrlClass

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""

    lateinit var ft : FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = ProfilMainFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as MainActivity

        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        urlClass = UrlClass()

        b.btnKeluarProfil.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Logout")
                .setMessage("Apakah Anda ingin keluar aplikasi?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,null)
                    prefEditor.putString(USERNAME,null)
                    prefEditor.putString(PASSWORD,null)
                    prefEditor.commit()

                    val intent = Intent(v.context, LoginActivity::class.java)
                    startActivity(intent)
                    ActivityCompat.finishAffinity(thisParent)
                    thisParent.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }

        b.btnEditProfilMain.setOnClickListener {
            var dialog = FragmentNamaEdit()
            val bundle = Bundle()
            bundle.putString("kode", "nama")
            dialog.show(childFragmentManager, "FragmentPasswordEdit")
            dialog.arguments = bundle
        }

        b.btnEmailProfil.setOnClickListener {
            var dialog = FragmentNamaEdit()
            val bundle = Bundle()

            bundle.putString("kode", "email")
            dialog.show(childFragmentManager, "FragmentPasswordEdit")
            dialog.arguments = bundle
        }

        b.btnLokasiProfil.setOnClickListener {
            var dialog = FragmentNamaEdit()
            val bundle = Bundle()
            bundle.putString("kode", "alamat")
            dialog.show(childFragmentManager, "FragmentPasswordEdit")
            dialog.arguments = bundle
        }

        b.btnKeamananProfil.setOnClickListener {
            val frag = PinFragment()
            val bundle = Bundle()
            bundle.putString("kode", "keamanan")
            frag.arguments = bundle
            frag.show(childFragmentManager, "")
        }

        b.btnKebijakanPrivasiProfil.setOnClickListener {
            FragmentPrivasi().show(childFragmentManager, "FragmentPrivasi")
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val nama = jsonObject.getString("nama")
                val noHp = jsonObject.getString("no_hp")
                val email = jsonObject.getString("email")
                val alamat = jsonObject.getString("alamat")

                b.tvNamaToko.setText(nama)
                b.tvNomorHandphoneToko.setText(noHp)
                b.tvEmailToko.setText(email)
                b.tvAlamatToko.setText(alamat)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("username",preferences.getString(USERNAME, DEF_USERNAME).toString())
                hm.put("password",preferences.getString(PASSWORD, DEF_PASSWORD).toString())

                when(mode) {
                    "show_profil" -> {
                        hm.put("mode", "show_profil")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}