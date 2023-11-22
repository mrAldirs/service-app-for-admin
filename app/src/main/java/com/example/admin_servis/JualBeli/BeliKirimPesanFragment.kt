package com.example.admin_servis.JualBeli

import android.os.Bundle
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
import kotlinx.android.synthetic.main.kirimpesan_fragment.view.*
import kotlinx.android.synthetic.main.beli_detail_activity.frameLayoutBeli
import org.json.JSONObject

class BeliKirimPesanFragment : Fragment() {
    lateinit var v: View
    lateinit var urlClass : UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.kirimpesan_fragment, container, false)

        val slide_bottom = AnimationUtils.loadAnimation(v.context, R.anim.translate_bottom)
        requireActivity().frameLayoutBeli.startAnimation(slide_bottom)
        requireActivity().frameLayoutBeli.animate()
            .setDuration(8000)

        val fragment = fragmentManager?.findFragmentById(R.id.frameLayoutBeli)
        urlClass = UrlClass()
        val bundle = arguments
        v.insPesanKodeTransaksi.setText(bundle?.get("kode").toString())
        v.insPesanNamaPelanggan.setText(bundle?.get("nama").toString())
        v.insPesanTopik.setText("Pembelian")

        v.btnBatalkan.setOnClickListener {
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                requireActivity().frameLayoutBeli.visibility = View.GONE
            }
        }

        v.btnTutup.setOnClickListener {
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                requireActivity().frameLayoutBeli.visibility = View.GONE
            }
        }

        v.btnKirimPesan.setOnClickListener {
            insertPesan("insert_pesan")
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                requireActivity().frameLayoutBeli.visibility = View.GONE
                Toast.makeText(v.context, "Berhasil Mengirim Pesan!", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    fun insertPesan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlNotifikasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode) {
                    "insert_pesan" -> {
                        hm.put("mode", "insert_pesan")
                        hm.put("nama", v.insPesanNamaPelanggan.text.toString())
                        hm.put("teks_notif", "Transaksi Kode "+v.insPesanKodeTransaksi.text.toString()+". "+v.insPesanTeks.text.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}