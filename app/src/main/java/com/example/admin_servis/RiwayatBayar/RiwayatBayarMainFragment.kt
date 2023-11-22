package com.example.admin_servis.RiwayatBayar

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.MainActivity
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import kotlinx.android.synthetic.main.main_bayar_fragment.frameRiwayatBayar
import kotlinx.android.synthetic.main.main_bayar_fragment.view.*
import org.json.JSONArray

class RiwayatBayarMainFragment : Fragment() {

    lateinit var thisParent : MainActivity
    lateinit var v : View
    lateinit var urlClass: UrlClass

    val dataBayar = mutableListOf<HashMap<String,String>>()
    lateinit var bayarAdapter: AdapterPembayaran

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as MainActivity
        v = inflater.inflate(R.layout.main_bayar_fragment, container, false)

        urlClass = UrlClass()
        bayarAdapter = AdapterPembayaran(dataBayar, this)
        v.rvRiwayatBayar.layoutManager = LinearLayoutManager(v.context)
        v.rvRiwayatBayar.adapter = bayarAdapter

        return v
    }

    override fun onStart() {
        super.onStart()
        readPembayaran("read_all_pembayaran")
    }

    private fun readPembayaran(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPembayaran,
            Response.Listener { response ->
                dataBayar.clear()
                if (response.equals(0)) {
                    Toast.makeText(this.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("tgl_pembayaran",jsonObject.getString("tgl_pembayaran"))
                        frm.put("total_bayar",jsonObject.getString("total_bayar"))
                        frm.put("status_pembayaran",jsonObject.getString("status_pembayaran"))
                        frm.put("jenis_transaksi",jsonObject.getString("jenis_transaksi"))
                        frm.put("kd_pembayaran",jsonObject.getString("kd_pembayaran"))

                        dataBayar.add(frm)
                    }
                    bayarAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(v.context)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "read_all_pembayaran" -> {
                        hm.put("mode","read_all_pembayaran")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    class AdapterPembayaran(private val dataBayar: List<HashMap<String,String>>, val parent: RiwayatBayarMainFragment ):
        RecyclerView.Adapter<AdapterPembayaran.HolderDataBayar>(){
        class HolderDataBayar(v: View) : RecyclerView.ViewHolder(v) {
            val cd = v.findViewById<CardView>(R.id.card)
            val tgl = v.findViewById<TextView>(R.id.transaksiTanggal)
            val kd = v.findViewById<TextView>(R.id.transaksiKode)
            val nm = v.findViewById<TextView>(R.id.transaksiNama)
            val jns = v.findViewById<TextView>(R.id.transaksiJenisTransaksi)
            val sts = v.findViewById<TextView>(R.id.transaksiStatus)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataBayar {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_transaksi, parent, false)
            return HolderDataBayar(v)
        }

        override fun getItemCount(): Int {
            return dataBayar.size
        }

        override fun onBindViewHolder(holder: HolderDataBayar, position: Int) {
            val data = dataBayar.get(position)

            holder.jns.setText("Total Pembayaran Rp."+data.get("total_bayar"))
            holder.tgl.setText(data.get("tgl_pembayaran"))
            holder.kd.setText(data.get("jenis_transaksi"))
            holder.kd.setTextColor(Color.parseColor("#000000"))
            holder.nm.setText(data.get("nama"))

            val status = data.get("status_pembayaran")
            if (status.toString().equals("Belum")) {
                holder.sts.setTextColor(Color.RED)
                holder.sts.setText("Belum Bayar")
            } else {
                holder.sts.setTextColor(Color.parseColor("#17ad96"))
                holder.sts.setText("Lunas")
            }

            holder.cd.setOnClickListener {
                var frag = RiwayatBayarDetailFragment()
                var paket : Bundle? = parent.requireActivity().intent.extras
                var kode = paket?.getString("kode")

                val bundle = Bundle()
                bundle.putString("kode", kode)
                bundle.putString("kode", data.get("kd_pembayaran").toString())
                frag.arguments = bundle

                parent.frameRiwayatBayar.visibility = View.VISIBLE
                parent.frameRiwayatBayar.setBackgroundColor(Color.argb(255,255,255,255))
                parent.childFragmentManager.beginTransaction()
                    .replace(R.id.frameRiwayatBayar, frag).commit()
            }
        }

    }
}