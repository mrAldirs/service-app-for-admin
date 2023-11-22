package com.example.admin_servis.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.MekanikModel
import com.example.admin_servis.R
import com.example.admin_servis.View.Servis.ServisKonfirmasiFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterMekanikList (private var dataList: List<MekanikModel>, val parent: ServisKonfirmasiFragment)
    : RecyclerView.Adapter<AdapterMekanikList.HolderDataTransaksi>() {
    class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.mekanikNama)
        val img = v.findViewById<CircleImageView>(R.id.mekanikPhoto)
        val pl = v.findViewById<AppCompatButton>(R.id.mekanikBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_mekanik_servis, parent, false)
        return HolderDataTransaksi(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
        val data = dataList.get(position)

        holder.nm.setText(data.nama_mekanik)
        Picasso.get().load(data.foto_mekanik).into(holder.img)

        holder.pl.setOnClickListener {
            parent.kdMekanik = data.kd_mekanik
            parent.binding.konfirmasiMekanik.setText(data.nama_mekanik)
        }
    }

    fun setData(newDataList: List<MekanikModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}