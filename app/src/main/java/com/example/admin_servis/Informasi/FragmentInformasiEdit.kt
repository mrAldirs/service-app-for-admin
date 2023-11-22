package com.example.admin_servis.Informasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.databinding.InformasiInsertFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
import org.json.JSONObject

class FragmentInformasiEdit : BottomSheetDialogFragment() {
    private lateinit var b: InformasiInsertFragmentBinding
    lateinit var thisParent : InformasiMainActivity
    lateinit var v : View
    lateinit var urlClass: UrlClass

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = InformasiInsertFragmentBinding.inflate(layoutInflater)
        v = b.root
        thisParent = activity as InformasiMainActivity

        val bundle = arguments
        b.insNamaPelangganInformasi.setText(bundle?.get("nama").toString())
        b.insTeksInformasi.setText(bundle?.get("teks").toString())

        urlClass = UrlClass()
        namaAdapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,daftarNama)
        b.insNamaPelangganInformasi.setAdapter(namaAdapter)
        b.insNamaPelangganInformasi.threshold = 1

        b.btnBatalkan.setOnClickListener { dismiss() }

        b.btnTutup.setOnClickListener { dismiss() }

        b.btnKirimInformasi.setOnClickListener {
            editInformasi("edit")
            dismiss()
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        getNama("get_nama")
    }

    private fun getNama(mode: String) {
        val request = object : StringRequest(
            Request.Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama"))
                }
                namaAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode) {
                    "get_nama" -> {
                        hm.put("mode", "get_nama")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    private fun editInformasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlNotifikasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    requireActivity().recreate()
                    Toast.makeText(v.context, "Berhasil Mengedit Informasi!", Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val bundle = arguments
                when(mode) {
                    "edit" -> {
                        hm.put("mode", "edit")
                        hm.put("kd_notif", bundle?.get("kode").toString())
                        hm.put("nama", b.insNamaPelangganInformasi.text.toString())
                        hm.put("teks_notif", b.insTeksInformasi.text.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}