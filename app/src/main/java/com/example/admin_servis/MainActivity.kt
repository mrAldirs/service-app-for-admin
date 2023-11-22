package com.example.admin_servis

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Barang.TerjualMainActivity
import com.example.admin_servis.View.Barang.TersediaMainActivity
import com.example.admin_servis.Feedback.FeedbackMainActivity
import com.example.admin_servis.Informasi.InformasiMainActivity
import com.example.admin_servis.JualBeli.PemesananMainActivity
import com.example.admin_servis.JualBeli.TransaksiMainActivity
import com.example.admin_servis.Profil.ProfilMainFragment
import com.example.admin_servis.Servis.ServisMainActivity
import com.example.admin_servis.View.Chat.ChatMainActivity
import com.example.admin_servis.View.Laporan.LaporanMainActivity
import com.example.admin_servis.View.User.UserMainActivity
import com.example.admin_servis.View.Pengiriman.PengirimanMainActivity
import com.example.admin_servis.View.Profil.FragmentPrivasi
import com.example.admin_servis.View.Servis.ServisRiwayatActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_activity.*
import org.json.JSONArray

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var profilMain : ProfilMainFragment
    lateinit var riwayatBayarMain : BayarMainFragment
    lateinit var ft : FragmentTransaction

    lateinit var urlClass: UrlClass

    val datftarTran = mutableListOf<HashMap<String,String>>()
    lateinit var tranAdapter: AdapterTransaksi

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.setTitle("Beranda Admin")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayoutAdmin)
        val navView : NavigationView = findViewById(R.id.nav_viewAdmin)

        toggle = ActionBarDrawerToggle(this, drawerLayoutAdmin, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profilMain = ProfilMainFragment()
        riwayatBayarMain = BayarMainFragment()
        bottomNavigationView1.setOnNavigationItemSelectedListener(this)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()
        tranAdapter = AdapterTransaksi(datftarTran)
        rvTransaksiBeranda.layoutManager = LinearLayoutManager(this)
        rvTransaksiBeranda.adapter = tranAdapter

        btnLihatSemuaTransaksiBeranda.setOnClickListener {
            startActivity(Intent(this, DetailMainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_homeAdmin -> {
                    supportActionBar?.setTitle("Home Admin")
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_informasiAdmin -> {
                    startActivity(Intent(this, InformasiMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_feddbackAdmin -> {
                    startActivity(Intent(this, FeedbackMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_userAdmin -> {
                    startActivity(Intent(this, UserMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_chatAdmin -> {
                    startActivity(Intent(this, ChatMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_barangAdmin -> {
                    startActivity(Intent(this, TersediaMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_barangTerjualAdmin -> {
                    startActivity(Intent(this, TerjualMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_servisAdmin -> {
                    startActivity(Intent(this, ServisMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_riwayatServis -> {
                    startActivity(Intent(this, ServisRiwayatActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_pemesananAdmin -> {
                    startActivity(Intent(this, PemesananMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_transaksiAdmin -> {
                    startActivity(Intent(this, TransaksiMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_pengirimanAdmin -> {
                    startActivity(Intent(this, PengirimanMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_resetAdmin -> {
                    FragmentPrivasi().show(supportFragmentManager, "FragmentPrivasi")
                }
                R.id.nav_laporanAdmin -> {
                    startActivity(Intent(this, LaporanMainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_logoutAdmin -> {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Logout")
                        .setMessage("Apakah Anda ingin keluar aplikasi?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            val prefEditor = preferences.edit()
                            prefEditor.putString(NAMA,null)
                            prefEditor.putString(USERNAME,null)
                            prefEditor.putString(PASSWORD,null)
                            prefEditor.commit()

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                        })
                        .show()
                    true
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menuBerandaAdmin -> {
                supportActionBar?.setTitle("Beranda Admin")
                frameLayoutAdmin.visibility = View.GONE
            }
            R.id.menuBayarAdmin -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayoutAdmin,riwayatBayarMain).commit()
                frameLayoutAdmin.setBackgroundColor(Color.argb(255,255,255,255))
                supportActionBar?.setTitle("Riwayat Pembayaran")
                frameLayoutAdmin.visibility = View.VISIBLE
            }
            R.id.menuProfilAdmin -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayoutAdmin,profilMain).commit()
                frameLayoutAdmin.setBackgroundColor(Color.argb(255,255,255,255))
                supportActionBar?.setTitle("Profil Toko")
                frameLayoutAdmin.visibility = View.VISIBLE
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        readTransaksi("pie_chart")
        showChart("pie_chart")
    }

    private fun showChart(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlMain,
            Response.Listener { response ->
                val entries = ArrayList<PieEntry>()
                val jsonArray = JSONArray(response)
                for (i in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(i)
                    val value = jsonObject.getInt("value")
                    val sts = jsonObject.getString("jenis_transaksi")
                    entries.add(PieEntry(value.toFloat(), sts))
                }

                setupPieChart(entries)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "pie_chart" -> {
                        hm.put("mode","pie_chart")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun setupPieChart(entries: ArrayList<PieEntry>) {
        val dataSet = PieDataSet(entries, "Data")
        dataSet.setColors(Color.parseColor("#068DA9"), Color.parseColor("#FFEF5350"), Color.parseColor("#FF42A5F5"), Color.parseColor("#C88EA7")) // Atur warna slice
        dataSet.setDrawValues(true)
        dataSet.valueFormatter = DefaultValueFormatter(0)

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false // Nonaktifkan deskripsi
        pieChart.setUsePercentValues(false)
        pieChart.legend.isEnabled = false
        pieChart.animateY(1000)
    }

    private fun readTransaksi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlMain,
            Response.Listener { response ->
                datftarTran.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
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
                AlertDialog.Builder(this)
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
                    "pie_chart" -> {
                        hm.put("mode","pie_chart")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterTransaksi(private val dataTransaksi: List<HashMap<String,String>> ):
        RecyclerView.Adapter<AdapterTransaksi.HolderDataTransaksi>(){
        class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
            val cd = v.findViewById<CardView>(R.id.card)
            val tra = v.findViewById<TextView>(R.id.mainTransaksi)
            val vl = v.findViewById<TextView>(R.id.mainJumlah)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_main, parent, false)
            return HolderDataTransaksi(v)
        }

        override fun getItemCount(): Int {
            return dataTransaksi.size
        }

        override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
            val data = dataTransaksi.get(position)

            holder.tra.setText(data.get("jenis_transaksi"))
            holder.vl.setText(data.get("value"))
            if(position.rem(2)==0) holder.cd.setCardBackgroundColor(Color.parseColor("#A5C0DD"))
            else holder.cd.setCardBackgroundColor(Color.parseColor("#6C9BCF"))
        }

    }

}