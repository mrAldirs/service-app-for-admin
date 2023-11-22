package com.example.admin_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.ServisModel
import com.example.admin_servis.R
import com.example.admin_servis.View.Servis.ServisDetailActivity

class AdapterServisRiwayat(private var dataList: List<ServisModel>)
    : RecyclerView.Adapter<AdapterServisRiwayat.HolderDataServis>() {
    class HolderDataServis(v: View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.adpNamaServis)
        val tgl = v.findViewById<TextView>(R.id.adpTanggalKirimServis)
        val krs = v.findViewById<TextView>(R.id.adpKerusakanServis)
        val kd = v.findViewById<TextView>(R.id.adpKodeServis)
        val sts = v.findViewById<TextView>(R.id.adpStatusServis)
        val cd = v.findViewById<CardView>(R.id.cardServis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataServis {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_servis, parent, false)
        return HolderDataServis(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataServis, position: Int) {
        val data = dataList.get(position)

        holder.nm.setText(data.nama_pelanggan)
        holder.tgl.setText(data.tgl_transaksi)
        holder.krs.setText(data.kerusakan)
        holder.kd.setText(data.kd_transaksi)

        val status = data.status_transaksi
        if (status.toString().equals("Belum")) {
            holder.sts.setText("baru")
        } else if (status.toString().equals("Proses")){
            holder.sts.setText("proses")
            holder.sts.setTextColor(Color.parseColor("#000000"))
        } else if (status.equals("Selesai")) {
            holder.sts.setTextColor(Color.parseColor("#17ad96"))
            holder.sts.setText("selesai")
        } else if (status.equals("Tolak")) {
            holder.sts.setTextColor(Color.RED)
            holder.sts.setText("ditolak")
        }

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, ServisDetailActivity::class.java)
            intent.putExtra("kode", data.kd_transaksi)
            it.context.startActivity(intent)
        }
    }

    fun setData(newDataList: List<ServisModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}