package com.example.admin_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.ImageDetailActivity
import com.example.admin_servis.Model.ChatModel
import com.example.admin_servis.R
import com.example.admin_servis.View.Chat.ChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterChat(private var dataList: List<ChatModel>, val parent: ChatActivity) :
    RecyclerView.Adapter<AdapterChat.HolderDataFeedback>(){
    class HolderDataFeedback (v : View) : RecyclerView.ViewHolder(v) {
        val tx = v.findViewById<TextView>( R.id.chatTeks)
        val nm = v.findViewById<TextView>(R.id.chatPengirim)
        val cd = v.findViewById<CardView>(R.id.card)
        val lingkaranP = v.findViewById<CircleImageView>(R.id.lingkaranPelanggan)
        val lingkaranA = v.findViewById<CircleImageView>(R.id.lingkaranAdmin)
        val jtgl = v.findViewById<TextView>(R.id.chatJamTanggal)
        val img = v.findViewById<ImageView>(R.id.chatImg)
        val cdI = v.findViewById<CardView>(R.id.cardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataFeedback {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_chat, parent, false)
        return HolderDataFeedback(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataFeedback, position: Int) {
        val data = dataList.get(position)
        holder.tx.setText(data.teks_chat)
        holder.nm.setText(data.nama)
        holder.jtgl.setText(data.tgl_chat)

        val imgV = data.gambar_chat

        if (imgV.equals("null")) {
            holder.cdI.visibility = View.GONE
        } else if (imgV.equals("")) {
            holder.cdI.visibility = View.GONE
        } else {
            holder.cdI.visibility = View.VISIBLE
            Picasso.get().load(data.gambar_chat).into(holder.img)
        }

        holder.img.setOnClickListener {
            val intent = Intent(it.context, ImageDetailActivity::class.java)
            intent.putExtra("img", data.gambar_chat)
            it.context.startActivity(intent)
            (it.context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        holder.cd.setOnLongClickListener { v: View ->
//            val contextMenu = PopupMenu(v.context, v)
//            contextMenu.menuInflater.inflate(R.menu.context_notif, contextMenu.menu)
//
//            if (parent.namaF.equals(data.nama)) {
//                contextMenu.menu.findItem(R.id.notifHapus)?.isVisible = true
//            } else {
//                contextMenu.menu.findItem(R.id.notifHapus)?.isVisible = false
//            }
//
//            contextMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.notifHapus -> {
//                        AlertDialog.Builder(v.context)
//                            .setIcon(R.drawable.warning)
//                            .setTitle("Peringatan!")
//                            .setMessage("Apakah Anda menghapus pesan ini?")
//                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
//                                parent.delete(data.kd_chat)
//                            })
//                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
//                            })
//                            .show()
//                        true
//                    }
//                }
//                false
//            }
//            contextMenu.show()
            true
        }

        if (data.nama.equals("Rukin Servis Elektronik")) {
            holder.nm.visibility = View.GONE
            holder.cd.setCardBackgroundColor(Color.parseColor("#CBFFAB"))
            Picasso.get().load(data.profil).into(holder.lingkaranA)
            holder.lingkaranP.visibility = View.GONE
        } else {
            holder.cd.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            Picasso.get().load(data.profil).into(holder.lingkaranP)
            holder.lingkaranA.visibility = View.GONE
        }
    }

    fun setData(newDataList: List<ChatModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}