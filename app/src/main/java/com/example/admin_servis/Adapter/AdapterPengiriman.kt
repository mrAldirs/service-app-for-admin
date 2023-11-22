package com.example.admin_servis.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.PengirimanModel
import com.example.admin_servis.R
import com.example.admin_servis.View.Pengiriman.PengirimanDetailActivity

class AdapterPengiriman (private var dataList: List<PengirimanModel>) :
    RecyclerView.Adapter<AdapterPengiriman.HolderDataAdapter>(){
    class HolderDataAdapter (v : View) : RecyclerView.ViewHolder(v) {
        val tgl = v.findViewById<TextView>(R.id.tanggalPengiriman)
        val mrk = v.findViewById<TextView>(R.id.merekPengiriman)
        val nm = v.findViewById<TextView>(R.id.namaPengirim)
        val alm = v.findViewById<TextView>(R.id.alamatPengiriman)
        val sts = v.findViewById<TextView>(R.id.statusPengiriman)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAdapter {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pengiriman,parent,false)
        return HolderDataAdapter(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataAdapter, position: Int) {
        val data = dataList.get(position)
        holder.mrk.setText(data.nama_barang)
        holder.nm.setText(data.nama)
        holder.alm.setText(data.alamat)

        if (data.tglkirim_jadwal.equals("null")) {
            holder.tgl.setText("tanggal kirim")
        } else {
            holder.tgl.setText(data.tglkirim_jadwal)
        }

        var sts = data.tglsampai_jadwal
        if (sts.equals("null")) {
            holder.sts.setText("Menunggu Anda mengirim Barang ke Lokasi.")
        } else {
            holder.sts.setText("Barang telah dikirim ke Alamat tujuan dari pelanggan.")
        }

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, PengirimanDetailActivity::class.java)
            intent.putExtra("alm", data.alamat)
            intent.putExtra("kode", data.kd_jadwal)
            it.context.startActivity(intent)
        }
    }

    fun setData(newHotelList: List<PengirimanModel>) {
        dataList = newHotelList
        notifyDataSetChanged()
    }
}