package com.example.admin_servis.View.Laporan

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.admin_servis.Helper.CurrencyHelper
import com.example.admin_servis.Model.LineModel
import com.example.admin_servis.R
import com.example.admin_servis.Repository.LaporanRepository
import com.example.admin_servis.ViewModel.LaporanViewModel
import com.example.admin_servis.ViewModel.LineViewModel
import com.example.admin_servis.databinding.LaporanMainActivityBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class LaporanMainActivity : AppCompatActivity() {
    private lateinit var b: LaporanMainActivityBinding
    private lateinit var lVM: LineViewModel
    private lateinit var lapVM: LaporanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = LaporanMainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        b.btnBack.setOnClickListener {
            onBackPressed()
        }

        lVM = ViewModelProvider(this).get(LineViewModel::class.java)
        lapVM = ViewModelProvider(this).get(LaporanViewModel::class.java)

        lVM.getTransaksi().observe(this, Observer { transaksiList ->
            createLineChart(transaksiList)
        })

        lapVM.laporanData.observe(this, Observer { laporanList ->
            for (laporan in laporanList) {
                when (laporan.jenisTransaksi) {
                    "Beli" -> {
                        b.tvBeli.text = CurrencyHelper.formatCurrency(laporan.laporan.toInt())
                        b.tvPengeluaran.text = CurrencyHelper.formatCurrency(laporan.laporan.toInt())
                    }
                    "Jual" -> b.tvJual.text = CurrencyHelper.formatCurrency(laporan.laporan.toInt())
                    "Servis" -> b.tvServis.text = CurrencyHelper.formatCurrency(laporan.laporan.toInt())
                }
            }
        })

        lapVM.getPendapatan().observe(this, Observer { (pendapatan, error) ->
            if (pendapatan == null) {
                b.tvPendapatan.setText("Rp.0")
            } else {
                b.tvPendapatan.text = CurrencyHelper.formatCurrency(pendapatan.pendapatan.toInt())
            }
        })

        lapVM.getLaporan()

        b.btnDetail.setOnClickListener {
            FragmentLaporanDetail().show(supportFragmentManager, "FragmentLaporanDetail")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun createLineChart(transaksiList: List<LineModel>) {
        val entriesBeli = mutableListOf<Entry>()
        val entriesJual = mutableListOf<Entry>()
        val entriesServis = mutableListOf<Entry>()

        val xValues = mutableListOf<String>()

        for (i in transaksiList.indices) {
            val transaksi = transaksiList[i]

            transaksi.beliTotal?.let { beliTotal ->
                entriesBeli.add(Entry(i.toFloat(), beliTotal))
            }

            transaksi.jualTotal?.let { jualTotal ->
                entriesJual.add(Entry(i.toFloat(), jualTotal))
            }

            transaksi.servisTotal?.let { servisTotal ->
                entriesServis.add(Entry(i.toFloat(), servisTotal))
            }


            xValues.add(transaksi.jenisTransaksi)
        }

        val dataSetBeli = LineDataSet(entriesBeli, "Beli")
        dataSetBeli.color = ContextCompat.getColor(this, R.color.purple_500)
        dataSetBeli.valueTextColor = Color.BLACK
        dataSetBeli.circleRadius = 0f
        dataSetBeli.setDrawFilled(true)
        dataSetBeli.fillColor = Color.parseColor("#80DEEA")
        dataSetBeli.fillAlpha = 30

        val dataSetJual = LineDataSet(entriesJual, "Jual")
        dataSetJual.color = Color.RED
        dataSetJual.valueTextColor = Color.BLACK
        dataSetJual.circleRadius = 0f
        dataSetJual.setDrawFilled(true)
        dataSetJual.fillColor = Color.parseColor("#FF9191")
        dataSetJual.fillAlpha = 30

        val dataSetServis = LineDataSet(entriesServis, "Servis")
        dataSetServis.color = Color.GREEN
        dataSetServis.valueTextColor = Color.BLACK
        dataSetServis.circleRadius = 0f
        dataSetServis.setDrawFilled(true)
        dataSetServis.fillColor = Color.parseColor("#C5E1A5")
        dataSetServis.fillAlpha = 30

        val lineDataSets = ArrayList<ILineDataSet>()
        lineDataSets.add(dataSetBeli)
        lineDataSets.add(dataSetJual)
        lineDataSets.add(dataSetServis)

        val lineData = LineData(lineDataSets)

        b.lineChart.data = lineData
        b.lineChart.setBackgroundColor(resources.getColor(R.color.white))
        b.lineChart.animateXY(5000, 5000)

        // Konfigurasi sumbu X
        val xAxis = b.lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(true)

        // Konfigurasi sumbu Y
        val yAxisLeft = b.lineChart.axisLeft
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight = b.lineChart.axisRight
        yAxisRight.isEnabled = false

        b.lineChart.description.isEnabled = false
        b.lineChart.invalidate()
    }
}