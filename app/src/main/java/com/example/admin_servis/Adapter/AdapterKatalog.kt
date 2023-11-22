package com.example.admin_servis.Adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.View.Barang.BarangDetailActivity
import com.example.admin_servis.View.Barang.FragmentTersediaEdit
import com.example.admin_servis.View.Barang.TersediaMainActivity
import com.example.admin_servis.Model.BarangModel
import com.example.admin_servis.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.brgtersedia_main_activity.btnInsertBarang

class AdapterKatalog(private var dataList: List<BarangModel>, val parent: TersediaMainActivity) :
    RecyclerView.Adapter<AdapterKatalog.HolderDataKatalog>(){
    class HolderDataKatalog(v: View) : RecyclerView.ViewHolder(v){
        val cd = v.findViewById<CardView>(R.id.card)
        val nmB = v.findViewById<TextView>(R.id.tersediaNama)
        val tgl = v.findViewById<TextView>(R.id.tersediaTanggal)
        val jns = v.findViewById<TextView>(R.id.tersediaJenis)
        val sts = v.findViewById<TextView>(R.id.tersediaStatusKatalog)
        val img = v.findViewById<ImageView>(R.id.tersediaPhoto)
        val edt = v.findViewById<Button>(R.id.btnEdit)
        val hps = v.findViewById<Button>(R.id.btnHapus)
        val dtt = v.findViewById<Button>(R.id.btnDetailTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKatalog {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_katalog, parent, false)
        return HolderDataKatalog(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataKatalog, position: Int) {
        val data = dataList.get(position)

        holder.nmB.setText(data.nama_barang)
        holder.tgl.setText(data.tgl_upload)
        holder.jns.setText(data.jenis_barang)
        Picasso.get().load(data.img_barang).into(holder.img)

        val stat = data.status_katalog
        if (stat.equals("Ready")) {
            holder.sts.setText("Barang Tersedia")
            holder.dtt.visibility = View.GONE
            holder.sts.setTextColor(Color.parseColor("#2046FF"))
        }

        holder.cd.setOnClickListener { v: View ->
            val intent = Intent(v.context, BarangDetailActivity::class.java)
            intent.putExtra("kdB", data.kd_barang)
            v.context.startActivity(intent)
        }

        holder.hps.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin menghapus data barang dari katalog?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    parent.delete(data.kd_barang)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        holder.edt.setOnClickListener {
            parent.btnInsertBarang.visibility = View.GONE

            var frag = FragmentTersediaEdit()

            val bundle = Bundle()
            bundle.putString("id", data.kd_barang)
            frag.arguments = bundle

            frag.show(parent.supportFragmentManager, "FragmentTersediaEdit")
        }
    }

    fun setData(newDataList: List<BarangModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}