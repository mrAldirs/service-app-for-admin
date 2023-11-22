package com.example.admin_servis.View.Servis

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.admin_servis.Model.ServisLineModel
import com.example.admin_servis.PageAdapter.ServisPage
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.ServisViewModel
import com.example.admin_servis.databinding.ServisRiwayatActivityBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ServisRiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ServisRiwayatActivityBinding
    private lateinit var servisViewModel: ServisViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ServisRiwayatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.adapter = ServisPage(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        servisViewModel = ViewModelProvider(this).get(ServisViewModel::class.java)

        servisViewModel.getLineChart().observe(this, Observer { servisList ->
            createLineChart(servisList)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun createLineChart(servisList: List<ServisLineModel>) {
        val entriesSelesai = mutableListOf<Entry>()
        val entriesDitolak = mutableListOf<Entry>()

        val xValues = mutableListOf<String>()

        for (i in servisList.indices) {
            val servis = servisList[i]

            servis.selesaiTotal?.let { selesaiTotal ->
                entriesSelesai.add(Entry(i.toFloat(), selesaiTotal))
            }

            servis.ditolakTotal?.let { ditolakTotal ->
                entriesDitolak.add(Entry(i.toFloat(), ditolakTotal))
            }

            xValues.add(servis.status_servis)
        }

        val dataSetSelesai = LineDataSet(entriesSelesai, "Selesai")
        dataSetSelesai.color = ContextCompat.getColor(this, R.color.purple_500)
        dataSetSelesai.valueTextColor = Color.BLACK
        dataSetSelesai.circleRadius = 0f
        dataSetSelesai.setDrawFilled(true)
        dataSetSelesai.fillColor = Color.parseColor("#80DEEA")
        dataSetSelesai.fillAlpha = 30

        val dataSetDitolak = LineDataSet(entriesDitolak, "Ditolak")
        dataSetDitolak.color = Color.RED
        dataSetDitolak.valueTextColor = Color.BLACK
        dataSetDitolak.circleRadius = 0f
        dataSetDitolak.setDrawFilled(true)
        dataSetDitolak.fillColor = Color.RED
        dataSetDitolak.fillAlpha = 30

        val lineDataSets = ArrayList<ILineDataSet>()
        lineDataSets.add(dataSetSelesai)
        lineDataSets.add(dataSetDitolak)

        val lineData = LineData(lineDataSets)

        binding.lineChart.data = lineData
        binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
        binding.lineChart.animateXY(5000, 5000)

        // Konfigurasi sumbu X
        val xAxis = binding.lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(true)

        // Konfigurasi sumbu Y
        val yAxisLeft = binding.lineChart.axisLeft
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight = binding.lineChart.axisRight
        yAxisRight.isEnabled = false

        binding.lineChart.description.isEnabled = false
        binding.lineChart.invalidate()
    }
}