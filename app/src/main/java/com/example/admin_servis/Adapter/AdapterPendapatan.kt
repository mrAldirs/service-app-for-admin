package com.example.admin_servis.Adapter

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_servis.Helper.CurrencyHelper
import com.example.admin_servis.Model.LabaRugi
import com.example.admin_servis.R
import com.example.admin_servis.View.Laporan.FragmentLaporanDetail

class AdapterPendapatan (private var dataList: List<LabaRugi>, val parent :  FragmentLaporanDetail) :
    RecyclerView.Adapter<AdapterPendapatan.HolderDataAdapter>() {
    class HolderDataAdapter(v: View) : RecyclerView.ViewHolder(v) {
        val bln = v.findViewById<TextView>(R.id.bulan)
        val pdt = v.findViewById<TextView>(R.id.pendapatan)
        val pgl = v.findViewById<TextView>(R.id.pengeluaran)
        val lb = v.findViewById<TextView>(R.id.total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAdapter {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pendapatan, parent, false)
        return HolderDataAdapter(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataAdapter, position: Int) {
        val data = dataList.get(position)
        val nmBulan = data.bulan
        val tahun = parent.nilaiSort
        if (nmBulan.equals("January")) {
            holder.bln.setText("Januari $tahun")
        } else if (nmBulan.equals("February")) {
            holder.bln.setText("Februari $tahun")
        } else if (nmBulan.equals("March")) {
            holder.bln.setText("Maret $tahun")
        } else if (nmBulan.equals("May")) {
            holder.bln.setText("Mei $tahun")
        } else if (nmBulan.equals("June")) {
            holder.bln.setText("Juni $tahun")
        } else if (nmBulan.equals("July")) {
            holder.bln.setText("Juli $tahun")
        } else if (nmBulan.equals("August")) {
            holder.bln.setText("Agustus $tahun")
        } else if (nmBulan.equals("October")) {
            holder.bln.setText("Oktober $tahun")
        } else if (nmBulan.equals("December")) {
            holder.bln.setText("Desember $tahun")
        } else {
            holder.bln.setText(nmBulan)
        }
        holder.pdt.text = CurrencyHelper.formatCurrency(data.total_pendapatan.toInt())
        holder.pgl.text = CurrencyHelper.formatCurrency(data.total_pengeluaran.toInt())

        val laba = CurrencyHelper.formatCurrency(data.laba.toInt())
        val labaText = "L/R : $laba"

        val spannableString = SpannableString(labaText)
        val labaColor = Color.parseColor("#0C25AE")
        val labaStartIndex = labaText.indexOf(laba)
        val labaEndIndex = labaStartIndex + laba.length
        spannableString.setSpan(ForegroundColorSpan(labaColor), labaStartIndex, labaEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.lb.text = spannableString
    }

    fun setData(newDataList: List<LabaRugi>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}