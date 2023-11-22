package com.example.admin_servis.Adapter

import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Feedback.FeedbackMainActivity
import com.example.admin_servis.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.feedback_main_activity.cardReplyMain
import kotlinx.android.synthetic.main.feedback_main_activity.namaReplyMain
import kotlinx.android.synthetic.main.feedback_main_activity.rvFeedback
import kotlinx.android.synthetic.main.feedback_main_activity.teksReplyMain
import kotlinx.android.synthetic.main.feedback_main_activity.txPesanFeedback

class AdapterFeedback(val dataFeeback: List<HashMap<String,String>>, val parent: FeedbackMainActivity) :
    RecyclerView.Adapter<AdapterFeedback.HolderDataFeedback>(){
    class HolderDataFeedback (v : View) : RecyclerView.ViewHolder(v) {
        val tx = v.findViewById<TextView>(R.id.teksFeedback)
        val nm = v.findViewById<TextView>(R.id.pengirimFeedback)
        val cd = v.findViewById<CardView>(R.id.cardFeedback)
        val lingkaranP = v.findViewById<CircleImageView>(R.id.lingkaranPelanggan)
        val lingkaranA = v.findViewById<CircleImageView>(R.id.lingkaranAdmin)
        val jtgl = v.findViewById<TextView>(R.id.jamtanggalFeedback)
        val txR = v.findViewById<TextView>(R.id.teksReply)
        val nmR = v.findViewById<TextView>(R.id.namaReply)
        val cdR = v.findViewById<CardView>(R.id.cardReply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataFeedback {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_feedback, parent, false)
        return HolderDataFeedback(v)
    }

    override fun getItemCount(): Int {
        return dataFeeback.size
    }

    override fun onBindViewHolder(holder: HolderDataFeedback, position: Int) {
        val data = dataFeeback.get(position)
        holder.tx.setText(data.get("teks_feedback"))
        holder.nm.setText(data.get("nama"))
        holder.jtgl.setText(data.get("jamtanggal_feedback"))

        val namaR = data.get("nama_reply")
        val teksR = data.get("teks_reply")

        if (namaR.toString().equals("null") && teksR.toString().equals("null")) {
            holder.cdR.visibility = View.GONE
        } else if (namaR.toString().equals("") && teksR.toString().equals("")) {
            holder.cdR.visibility = View.GONE
        } else {
            holder.cdR.visibility = View.VISIBLE
            holder.txR.setText(data.get("teks_reply"))
            holder.nmR.setText(data.get("nama_reply"))
        }

        holder.cd.setOnLongClickListener {
            parent.idF = data.get("kd_feedback").toString()

            val contextMenu = PopupMenu(parent, it)
            contextMenu.menuInflater.inflate(R.menu.context_feedback, contextMenu.menu)
            contextMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.feedbackReply -> {
                        parent.cardReplyMain.visibility = View.VISIBLE
                        parent.namaReplyMain.text = data?.get("nama").toString()
                        parent.teksReplyMain.text = data?.get("teks_feedback").toString()
                        parent.txPesanFeedback.requestFocus()
                    }
                    R.id.feedbackHapus -> {
                        AlertDialog.Builder(parent)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan!")
                            .setMessage("Apakah Anda menghapus pesan ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                parent.delete("delete")
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

        val lvl = data.get("level")
        if (lvl.toString().equals("Pelanggan")) {
            holder.cd.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            Picasso.get().load(data.get("profil")).into(holder.lingkaranP)
            holder.lingkaranA.visibility = View.GONE
        } else {
            holder.nm.visibility = View.GONE
            holder.cd.setCardBackgroundColor(Color.parseColor("#CBFFAB"))
            Picasso.get().load(data.get("profil")).into(holder.lingkaranA)
            holder.lingkaranP.visibility = View.GONE
        }
    }
}