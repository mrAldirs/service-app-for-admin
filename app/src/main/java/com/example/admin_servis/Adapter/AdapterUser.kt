package com.example.admin_servis.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Model.UserModel
import com.example.admin_servis.R
import com.example.admin_servis.View.User.FragmentUserDetail
import com.example.admin_servis.User.FragmentUserEdit
import com.example.admin_servis.View.User.UserMainActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterUser(private var dataList: List<UserModel>, val parent: UserMainActivity) :
    RecyclerView.Adapter<AdapterUser.HolderDataUser>(){
    class HolderDataUser(v : View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<CircleImageView>(R.id.userImage)
        val nm = v.findViewById<TextView>(R.id.userNama)
        val hp = v.findViewById<TextView>(R.id.userNoHp)
        val cd = v.findViewById<CardView>(R.id.cardUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataUser {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return HolderDataUser(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataUser, position: Int) {
        val data = dataList.get(position)
        holder.nm.setText(data.nama)

        val no = data.no_hp
        val im = data.profil

        if (no.equals("null")) {
            holder.hp.setText("+62")
        } else {
            holder.hp.setText(data.no_hp)
        }

        if (im.equals("null")) {
            Picasso.get().load(R.drawable.akun).into(holder.img)
        } else {
            Picasso.get().load(data.profil).into(holder.img)
        }

        holder.cd.setOnClickListener {
            var frag = FragmentUserDetail()
            var paket : Bundle? = parent.intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("kode", data.kd_user)
            frag.arguments = bundle

            frag.show(parent.supportFragmentManager, "FragmentUserDetail")
        }

        holder.cd.setOnLongClickListener {
            val contextMenu = PopupMenu(parent, it)
            contextMenu.menuInflater.inflate(R.menu.context_user, contextMenu.menu)
            contextMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.user_edit -> {
                        var frag = FragmentUserEdit()
                        var paket : Bundle? = parent.intent.extras
                        var kode = paket?.getString("kode")

                        val bundle = Bundle()
                        bundle.putString("kode", kode)
                        bundle.putString("kode", data.kd_user)
                        frag.arguments = bundle

                        frag.show(parent.supportFragmentManager, "FragmentUserEdit")
                    }
                }
                false
            }
            contextMenu.show()
            true
        }
    }

    fun setData(newDataList: List<UserModel>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}