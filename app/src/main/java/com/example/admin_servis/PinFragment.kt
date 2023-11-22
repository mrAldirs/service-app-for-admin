package com.example.admin_servis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Profil.FragmentPasswordEdit
import com.example.admin_servis.Profil.FragmentPinEdit
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.pin_fragment.view.*
import kotlinx.android.synthetic.main.profil_main_fragment.*
import org.json.JSONObject

class PinFragment : BottomSheetDialogFragment() {
    lateinit var thisParent: MainActivity
    lateinit var v: View

    lateinit var urlClass: UrlClass

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""

    var kode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as MainActivity
        v = inflater.inflate(R.layout.pin_fragment, container, false)

        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()

        v.btnBatalkan.setOnClickListener {
            dismiss()
            v.txPin1.setText("")
            v.txPin2.setText("")
            v.txPin3.setText("")
            v.txPin4.setText("")
            v.txPin5.setText("")
            v.txPin6.setText("")
        }

        v.btnTutup.setOnClickListener {
            dismiss()
            v.txPin1.setText("")
            v.txPin2.setText("")
            v.txPin3.setText("")
            v.txPin4.setText("")
            v.txPin5.setText("")
            v.txPin6.setText("")
        }

        v.txPin1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    v.txPin2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        v.txPin2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    v.txPin3.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        v.txPin3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    v.txPin4.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        v.txPin4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    v.txPin5.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        v.txPin5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 1) {
                    v.txPin6.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        v.btnKirimPin.setOnClickListener {
            showProfil("show_profil")
        }

        return v
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val pin = jsonObject.getString("pin")

                var etPin = v.txPin1.text.toString() + v.txPin2.text.toString() + v.txPin3.text.toString() + v.txPin4.text.toString() +
                        v.txPin5.text.toString() + v.txPin6.text.toString()

                kode = arguments?.getString("kode").toString()
                if (etPin.equals(pin)) {
                    if (kode.equals("keamanan")) {
                        var dialog = FragmentPasswordEdit()

                        dialog.show(childFragmentManager, "FragmentPasswordEdit")
                    } else if (kode.equals("editPin")) {
                        var dialog = FragmentPinEdit()

                        dialog.show(childFragmentManager, "FragmentPinEdit")
                    }
                }
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