package com.example.admin_servis.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.ChatModel
import com.example.admin_servis.R
import com.example.admin_servis.View.Chat.ChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterChatPelanggan(private var dataList: List<ChatModel>) :
    RecyclerView.Adapter<AdapterChatPelanggan.HolderDataUser>(){
    class HolderDataUser(v : View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<CircleImageView>(R.id.chatImage)
        val nm = v.findViewById<TextView>(R.id.chatNama)
        val tks = v.findViewById<TextView>(R.id.chatPesan)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataUser {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_chat_pelanggan, parent, false)
        return HolderDataUser(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataUser, position: Int) {
        val data = dataList.get(position)
        holder.nm.setText(data.nama)
        holder.tks.setText(data.teks_chat)
        val im = data.profil

        if (im.equals("null")) {
            Picasso.get().load(R.drawable.akun).into(holder.img)
        } else {
            Picasso.get().load(data.profil).into(holder.img)
        }

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, ChatActivity::class.java)
            intent.putExtra("kode", data.kd_chat)
            intent.putExtra("topik", "Chat")
            it.context.startActivity(intent)
            (it.context as AppCompatActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

//        holder.cd.setOnLongClickListener {
//            val contextMenu = PopupMenu(parent, it)
//            contextMenu.menuInflater.inflate(R.menu.context_user, contextMenu.menu)
//            contextMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.user_edit -> {
//                        var frag = FragmentUserEdit()
//                        var paket : Bundle? = parent.intent.extras
//                        var kode = paket?.getString("kode")
//
//                        val bundle = Bundle()
//                        bundle.putString("kode", kode)
//                        bundle.putString("kode", data.kd_user)
//                        frag.arguments = bundle
//
//                        frag.show(parent.supportFragmentManager, "FragmentUserEdit")
//                    }
//                }
//                false
//            }
//            contextMenu.show()
//            true
//        }
    }

    fun setData(newDataList: List<ChatModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}