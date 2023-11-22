package com.example.admin_servis.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.R

class AdapterAkun(val dataAkun: List<HashMap<String,String>>) :
    RecyclerView.Adapter<AdapterAkun.HolderDataAkun>(){
    class HolderDataAkun(v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.akunNama)
        val usr = v.findViewById<TextView>(R.id.akunUsername)
        val pw = v.findViewById<TextView>(R.id.akunPassword)
        val lv = v.findViewById<TextView>(R.id.akunLevel)
        val sts = v.findViewById<View>(R.id.akunStatus)
        val cd = v.findViewById<CardView>(R.id.cardAkun)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAkun {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_akun, parent, false)
        return HolderDataAkun(v)
    }

    override fun getItemCount(): Int {
        return dataAkun.size
    }

    override fun onBindViewHolder(holder: HolderDataAkun, position: Int) {
        val data = dataAkun.get(position)
        holder.nm.setText(data.get("nama"))
        holder.usr.setText("Username : "+data.get("username"))
        holder.pw.setText("Password : "+data.get("password"))
        holder.lv.setText(data.get("level"))

        val st = data.get("sts_akun")
        if (st.toString().equals("AKTIF")) {
            holder.sts.setBackgroundColor(Color.parseColor("#09E812"))
        } else {
            holder.sts.visibility = View.GONE
        }
    }
}