package com.example.admin_servis.User

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.View.User.UserMainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import org.json.JSONObject

class FragmentUserEdit : BottomSheetDialogFragment() {
    lateinit var thisParent : UserMainActivity
    lateinit var v : View
    lateinit var urlClass : UrlClass
    var kdU = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as UserMainActivity
        v = inflater.inflate(R.layout.user_edit_fragment, container, false)

        urlClass = UrlClass()

        v.btnBatalkan.setOnClickListener {
            dismiss()
        }

        v.btnTutup.setOnClickListener {
            dismiss()
        }

        v.txUsername.visibility = View.GONE
        v.txPassoword.visibility = View.GONE
        v.divider2.visibility = View.GONE

        v.btnSimpanEditUser.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah yakin Anda ingin mengedit profil user?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    editUser("edit")
                    dismiss()
                    requireActivity().recreate()
                    Toast.makeText(this.context,"Berhasil mengedit profil user!", Toast.LENGTH_LONG).show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detailUser("detail")
    }

    private fun detailUser(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("nama")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("email")
                val st5 = jsonObject.getString("usia")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("profil")

                kdU = st1
                v.tvKdEditUser.setText("Kode "+st1)
                v.txNamaEditUser.setText(st2)

                if (!st3.toString().equals("null") || !st4.toString().equals("null") || !st5.toString().equals("null") ||
                    !st6.toString().equals("null")) {
                    v.txAlamatEditUser.setText(st3)
                    v.txEmailEditUser.setText(st4)
                    v.txUsiaEditUser.setText(st5)
                    v.txNoHpEditUser.setText(st6)
                } else {
                    v.txAlamatEditUser.setText("")
                    v.txEmailEditUser.setText("")
                    v.txUsiaEditUser.setText("")
                    v.txNoHpEditUser.setText("")
                }

                if (st7.toString().equals("null")) {
                    Picasso.get().load(R.drawable.akun).into(v.imgEditUser)
                } else {
                    Picasso.get().load(st7).into(v.imgEditUser)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode) {
                    "detail" -> {
                        hm.put("mode", "detail")
                        hm.put("kd_user", bundle?.get("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun editUser(mode: String){
        val request = object : StringRequest(
            Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "edit" -> {
                        hm.put("mode", "edit")
                        hm.put("kd_user", kdU)
                        hm.put("nama", v.txNamaEditUser.text.toString())
                        hm.put("alamat", v.txAlamatEditUser.text.toString())
                        hm.put("email", v.txEmailEditUser.text.toString())
                        hm.put("usia", v.txUsiaEditUser.text.toString())
                        hm.put("no_hp", v.txNoHpEditUser.text.toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

}