package com.example.admin_servis.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.JualBeli.BeliDetailActivity
import com.example.admin_servis.JualBeli.FragmentTransaksiJual
import com.example.admin_servis.JualBeli.JualDetailActivity
import com.example.admin_servis.JualBeli.TransaksiMainActivity
import com.example.admin_servis.R
import com.squareup.picasso.Picasso

class AdapterJual(val dataJual: List<HashMap<String,String>>, val parent: FragmentTransaksiJual) :
    RecyclerView.Adapter<AdapterJual.HolderDataJual>(){
    class HolderDataJual (v: View) : RecyclerView.ViewHolder(v) {
        val cd = v.findViewById<CardView>(R.id.card)
        val nmP = v.findViewById<TextView>(R.id.beliNamaPelanggan)
        val tgl = v.findViewById<TextView>(R.id.beliTanggalTransaksi)
        val nmB = v.findViewById<TextView>(R.id.beliNamaBarang)
        val sts = v.findViewById<TextView>(R.id.beliStatusTransaksi)
        val img = v.findViewById<ImageView>(R.id.beliImage)
        val dtt = v.findViewById<Button>(R.id.beliBtnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataJual {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_beli, parent, false)
        return HolderDataJual(v)
    }

    override fun getItemCount(): Int {
        return dataJual.size
    }

    override fun onBindViewHolder(holder: HolderDataJual, position: Int) {
        val data = dataJual.get(position)

        holder.nmP.setText(data.get("nama"))
        holder.tgl.setText(data.get("tgl_transaksi"))
        holder.nmB.setText(data.get("nama_barang"))
        Picasso.get().load(data.get("img_barang")).into(holder.img)

        val status = data.get("status_transaksi")
        if (status.toString().equals("Belum")) {
            holder.sts.setText("belum bayar")
            holder.sts.setTextColor(Color.RED)
        } else if (status.toString().equals("Proses")) {
            holder.sts.setText("konfirmasi kirim")
            holder.sts.setTextColor(Color.BLUE)
        } else {
            holder.sts.setTextColor(Color.parseColor("#17ad96"))
            holder.sts.setText("selesai")

            holder.cd.setOnLongClickListener {
                val contextMenu = PopupMenu(parent.requireContext(), it)
                contextMenu.menuInflater.inflate(R.menu.context_hapus, contextMenu.menu)
                contextMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.contextHapus -> {
                            AlertDialog.Builder(parent.requireContext())
                                .setIcon(R.drawable.warning)
                                .setTitle("Peringatan!")
                                .setMessage("Apakah Anda menghapus transaksi ini?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                    parent.delete(data.get("kd_transaksi").toString(), data.get("kd_barang").toString())
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                })
                                .show()
                            true
                        }
                    }
                    false
                }
                contextMenu.show()
                true
            }
        }

        holder.dtt.setOnClickListener {
            val intent = Intent(it.context, JualDetailActivity::class.java)
            intent.putExtra("kode", data.get("kd_transaksi").toString())
            it.context.startActivity(intent)
            parent.requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }
    }
}