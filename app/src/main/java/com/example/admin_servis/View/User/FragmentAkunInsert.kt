package com.example.admin_servis.View.User

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.akun_insert_fragment.view.*
import org.json.JSONObject

class FragmentAkunInsert : BottomSheetDialogFragment() {
    lateinit var v: View
    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.akun_insert_fragment, container, false)

        urlClass = UrlClass()

        v.btnBatalkan.setOnClickListener {
            dismiss()
        }

        v.btnTutup.setOnClickListener {
            dismiss()
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
                        insertAkun("insert")
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
                    dismiss()
                    requireActivity().recreate()
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