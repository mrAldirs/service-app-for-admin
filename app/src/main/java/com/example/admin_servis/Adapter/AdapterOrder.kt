package com.example.admin_servis.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.JualBeli.PemesananDetailActivity
import com.example.admin_servis.JualBeli.PemesananMainActivity
import com.example.admin_servis.R

class AdapterOrder(val dataOrder: List<HashMap<String,String>>, val parent: PemesananMainActivity) :
    RecyclerView.Adapter<AdapterOrder.HolderDataOrder>(){
    class HolderDataOrder (v: View) : RecyclerView.ViewHolder(v) {
        val cd = v.findViewById<CardView>(R.id.card)
        val tgl = v.findViewById<TextView>(R.id.transaksiTanggal)
        val kd = v.findViewById<TextView>(R.id.transaksiKode)
        val nm = v.findViewById<TextView>(R.id.transaksiNama)
        val nmB = v.findViewById<TextView>(R.id.transaksiJenisTransaksi)
        val sts = v.findViewById<TextView>(R.id.transaksiStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataOrder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_transaksi, parent, false)
        return HolderDataOrder(v)
    }

    override fun getItemCount(): Int {
        return dataOrder.size
    }

    override fun onBindViewHolder(holder: HolderDataOrder, position: Int) {
        val data = dataOrder.get(position)

        holder.nmB.setText("Order : "+data.get("nama_barang"))
        holder.tgl.setText(data.get("tgl_transaksi"))
        holder.kd.setText(data.get("kd_transaksi"))
        holder.nm.setText(data.get("nama"))

        val status = data.get("status_transaksi")
        if (status.toString().equals("Belum")) {
            holder.sts.setText("baru")
        } else if (status.toString().equals("Proses")){
            holder.sts.setText("proses")
            holder.sts.setTextColor(Color.parseColor("#000000"))
        } else if (status.toString().equals("Tolak")) {
            holder.sts.setTextColor(Color.RED)
            holder.sts.setText("dibatalkan")
        }else {
            holder.sts.setTextColor(Color.parseColor("#17ad96"))
            holder.sts.setText("selesai")

            holder.cd.setOnLongClickListener {
                val contextMenu = PopupMenu(parent, it)
                contextMenu.menuInflater.inflate(R.menu.context_hapus, contextMenu.menu)
                contextMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.contextHapus -> {
                            AlertDialog.Builder(parent)
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

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, PemesananDetailActivity::class.java)
            intent.putExtra("kode", data.get("kd_transaksi").toString())
            it.context.startActivity(intent)
        }
    }
}