package com.example.admin_servis.Adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.UserTransaksiMain
import com.example.admin_servis.R

class AdapterDetailMain (private var dataList: List<UserTransaksiMain>) :
    RecyclerView.Adapter<AdapterDetailMain.HolderDataAdapter>(){
    class HolderDataAdapter (v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.mainNama)
        val srv = v.findViewById<TextView>(R.id.mainServis)
        val ord = v.findViewById<TextView>(R.id.mainOrder)
        val jl = v.findViewById<TextView>(R.id.mainJual)
        val bl = v.findViewById<TextView>(R.id.mainBeli)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAdapter {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_detail_main,parent,false)
        return HolderDataAdapter(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: HolderDataAdapter, position: Int) {
        val data = dataList.get(position)
        holder.nm.setText(data.nama)
        holder.srv.text = "Servis : "+data.transaksi.getOrDefault("Servis", "0")+" Kali"
        holder.ord.text = "Order : "+data.transaksi.getOrDefault("Order", "0")+" Kali"
        holder.jl.text = "Jual : "+data.transaksi.getOrDefault("Jual", "0")+" Kali"
        holder.bl.text = "Beli : "+data.transaksi.getOrDefault("Beli", "0")+" Kali"
    }

    fun setData(newDataList: List<UserTransaksiMain>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}