package com.example.admin_servis.RiwayatBayar

import android.graphics.Color
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.riwayatbayar_detail_fragment.view.*
import kotlinx.android.synthetic.main.main_bayar_fragment.frameRiwayatBayar
import org.json.JSONObject

class RiwayatBayarDetailFragment : Fragment() {
    lateinit var urlClass : UrlClass
    lateinit var v: View
    var kdByr = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.riwayatbayar_detail_fragment, container, false)

        urlClass = UrlClass()

        val fragment = fragmentManager?.findFragmentById(R.id.frameRiwayatBayar)
        val slide_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.translate_bottom)
        requireParentFragment().frameRiwayatBayar.startAnimation(slide_bottom)

        val bundle = arguments
        v.riwayatBayarKode.text = "Kode : "+bundle?.get("kode").toString()

        v.btnBatalkan.setOnClickListener {
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                requireParentFragment().frameRiwayatBayar.visibility = View.GONE
            }
        }

        v.btnTutup.setOnClickListener {
            if (fragment != null) {
                fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                requireParentFragment().frameRiwayatBayar.visibility = View.GONE
            }
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        detailPembayaran("detail_pembayaran")
    }

    private fun detailPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val kdPb = jsonObject.getString("kd_pembayaran")
                val tgl = jsonObject.getString("tgl_pembayaran")
                val nama = jsonObject.getString("nama")
                val noHp = jsonObject.getString("no_hp")
                val alm = jsonObject.getString("alamat")
                val brg = jsonObject.getString("nama_barang")
                val byr = jsonObject.getString("bayar")
                val tran = jsonObject.getString("jenis_transaksi")
                val adm = jsonObject.getString("biaya_admin")
                val ong = jsonObject.getString("ongkir")
                val tot = jsonObject.getString("total_bayar")
                val sts = jsonObject.getString("status_pembayaran")
                val rek = jsonObject.getString("rek_payment")
                val img = jsonObject.getString("img_pembayaran")

                kdByr = kdPb
                v.riwayatBayarTanggal.setText(tgl)
                v.riwayatBayarNama.setText(nama)
                v.riwayatBayarNoHp.setText(noHp)
                v.riwayatBayarAlamat.setText(alm)
                v.riwayatBayarNamaBarang.setText(brg)
                v.riwayatBayarBayar.setText("Rp."+byr)
                v.riwayatBayarJenis.setText(tran)
                v.riwayatBayarBiayaAdmin.setText("Rp."+adm)
                v.riwayatBayarOngkir.setText("Rp."+ong)
                v.riwayatBayarTotal.setText("Rp."+tot)

                if (sts.equals("Belum")) {
                    v.riwayatBayarStatusBayar.setText("Belum Bayar")
                    v.riwayatBayarStatusBayar.setTextColor(Color.RED)
                } else {
                    v.riwayatBayarStatusBayar.setText("Lunas")
                    v.riwayatBayarStatusBayar.setTextColor(Color.parseColor("#17ad96"))
                }
                v.riwayatBayarRekPayment.setText(rek)
                Picasso.get().load(img).into(v.riwayatBayarImage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode) {
                    "detail_pembayaran" -> {
                        hm.put("mode", "detail_pembayaran")
                        hm.put("kd_pembayaran", bundle?.get("kode").toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}