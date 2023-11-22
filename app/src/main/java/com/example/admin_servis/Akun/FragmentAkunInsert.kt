package com.example.admin_servis.Akun

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.akun_insert_fragment.view.*
import kotlinx.android.synthetic.main.akun_main_activity.*
import org.json.JSONObject

class FragmentAkunInsert : Fragment() {
    lateinit var thisParent: AkunMainActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as AkunMainActivity
        v = inflater.inflate(R.layout.akun_insert_fragment, container, false)

        urlClass = UrlClass()

        val slide_bottom = AnimationUtils.loadAnimation(thisParent, R.anim.translate_bottom)
        thisParent.frameLayoutInsertAkun.startAnimation(slide_bottom)

        val fragment = fragmentManager?.findFragmentById(R.id.frameLayoutInsertAkun)

        v.btnBatalkan.setOnClickListener {
            fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit() }
            thisParent.btnInsertAkun.visibility = View.VISIBLE
            thisParent.frameLayoutInsertAkun.visibility = View.GONE
        }

        v.btnTutup.setOnClickListener {
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                thisParent.btnInsertAkun.visibility = View.VISIBLE
                thisParent.frameLayoutInsertAkun.visibility = View.GONE
            }
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

        v.btnSimpanInsertAkun.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Info!")
                .setMessage("Apakah Anda ingin mendaftarkan Akun Baru bagi pelanggan?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!v.txNamaAkunInsert.text.toString().equals("") && !v.txUsernameAkunInsert.text.toString().equals("") && !v.txPasswordAkunInsert.text.toString().equals("")) {
                        if (fragment != null) {
                            insertAkun("insert")
                            fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                            thisParent.btnInsertAkun.visibility = View.VISIBLE
                            thisParent.frameLayoutInsertAkun.visibility = View.GONE
                            thisParent.recreate()
//                            Toast.makeText(v.context, "Berhasil Menambahkan Informasi!", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    fun insertAkun(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkun,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil mendaftarkan Akun!", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(v.context, "Gagal mendaftarkan Akun!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val pinResult = v.txPin1.text.toString() + v.txPin2.text.toString() + v.txPin3.text.toString() + v.txPin4.text.toString() +
                        v.txPin5.text.toString() + v.txPin6.text.toString()
                when(mode){
                    "insert" -> {
                        hm.put("mode","insert")
                        hm.put("nama", v.txNamaAkunInsert.text.toString())
                        hm.put("username", v.txUsernameAkunInsert.text.toString())
                        hm.put("password", v.txPasswordAkunInsert.text.toString())
                        hm.put("pin", pinResult)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}