package com.example.admin_servis

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.View.Laporan.LaporanMainActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.main_bayar_fragment.view.*
import org.json.JSONArray

class BayarMainFragment : Fragment() {

    lateinit var thisParent: MainActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    val datftarTran = mutableListOf<HashMap<String,String>>()
    lateinit var tranAdapter: AdapterTransaksi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as MainActivity
        v = inflater.inflate(R.layout.main_bayar_fragment, container, false)

        urlClass = UrlClass()
        tranAdapter = AdapterTransaksi(datftarTran)
        v.rvRiwayatBayar.layoutManager = LinearLayoutManager(this.context)
        v.rvRiwayatBayar.adapter = tranAdapter

        v.btnLihatSemuaRiwayatBayar.setOnClickListener {
            startActivity(Intent(v.context, LaporanMainActivity::class.java))
            thisParent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        showChart("bar_chart")
        readTransaksi("bar_chart")

        return v
    }

    private fun showChart(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlMain,
            Response.Listener { response ->
                val barEntries = ArrayList<BarEntry>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val value = jsonObject.getDouble("value").toFloat()
                    val sts = jsonObject.getString("jenis_transaksi")
                    barEntries.add(BarEntry(i.toFloat(), value, sts))
                }

                setupBarChart(barEntries)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "bar_chart" -> {
                        hm.put("mode","bar_chart")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    private fun setupBarChart(barEntries: List<BarEntry>) {
        val dataSet = BarDataSet(barEntries, "Data Set")
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(dataSet)

        val data = BarData(dataSets)

        val barChart: BarChart = v.findViewById(R.id.barChart)
        barChart.data = data
        barChart.description.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.setDrawLabels(true)
        barChart.xAxis.labelCount = barEntries.size

        // Menambahkan label jenis transaksi pada sumbu X
        val labels = ArrayList<String>()
        for (entry in barEntries) {
            labels.add(entry.data as String)
        }
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.animateY(1000, Easing.EaseInOutCubic)

        barChart.invalidate()
    }

    private fun readTransaksi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlMain,
            Response.Listener { response ->
                datftarTran.clear()
                if (response.equals(0)) {
                    Toast.makeText(this.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("jenis_transaksi",jsonObject.getString("jenis_transaksi"))
                        frm.put("value",jsonObject.getString("value"))

                        datftarTran.add(frm)
                    }
                    tranAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(v.context)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "bar_chart" -> {
                        hm.put("mode","bar_chart")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    class AdapterTransaksi(private val dataTransaksi: List<HashMap<String,String>> ):
        RecyclerView.Adapter<AdapterTransaksi.HolderDataTransaksi>(){
        class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
            val cd = v.findViewById<CardView>(R.id.card)
            val tra = v.findViewById<TextView>(R.id.mainBayar)
            val vl = v.findViewById<TextView>(R.id.mainJumlahBayar)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_main_bayar, parent, false)
            return HolderDataTransaksi(v)
        }

        override fun getItemCount(): Int {
            return dataTransaksi.size
        }

        override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
            val data = dataTransaksi.get(position)

            holder.tra.setText(data.get("jenis_transaksi"))
            holder.vl.setText("Rp."+data.get("value"))
            if(position.rem(2)==0) holder.cd.setCardBackgroundColor(Color.parseColor("#A5C0DD"))
            else holder.cd.setCardBackgroundColor(Color.parseColor("#6C9BCF"))
        }

    }
}