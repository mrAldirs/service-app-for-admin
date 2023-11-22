package com.example.admin_servis.View.Servis

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterMekanikList
import com.example.admin_servis.Helper.BaseApplication
import com.example.admin_servis.R
import com.example.admin_servis.Servis.ServisMainActivity
import com.example.admin_servis.UrlClass
import com.example.admin_servis.ViewModel.MekanikViewModel
import com.example.admin_servis.databinding.ServisKonfirmasiFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
import java.util.Calendar

class ServisKonfirmasiFragment : BottomSheetDialogFragment() {
    lateinit var binding: ServisKonfirmasiFragmentBinding
    lateinit var thisParent : ServisDetailActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass
    private lateinit var mekanikViewModel: MekanikViewModel
    lateinit var adapter: AdapterMekanikList

    var konfirmasiText = ""
    var kdMekanik = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ServisKonfirmasiFragmentBinding.inflate(layoutInflater)
        v = binding.root
        thisParent = activity as ServisDetailActivity

        urlClass = UrlClass()
        mekanikViewModel = ViewModelProvider(this).get(MekanikViewModel::class.java)
        adapter = AdapterMekanikList(ArrayList(), this)
        binding.rvMekanik.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMekanik.adapter = adapter

        binding.btnBatalkan.setOnClickListener {
            dismiss()
        }

        binding.btnTutup.setOnClickListener {
            dismiss()
        }

        binding.konfirmasiJamServis.setOnClickListener {
            showTimePickerDialog()
        }

        binding.konfirmasiTanggalServis.setOnClickListener {
            showDatePickerDialog()
        }

        binding.rgKonfirmasi.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.konfirmasiTerima -> {
                    konfirmasiText = "terima"
                    binding.textCat.visibility = View.VISIBLE
                    binding.inputMekanik.visibility = View.VISIBLE
                    binding.tvMekanik.visibility = View.VISIBLE
                    binding.rvMekanik.visibility = View.VISIBLE
                }
                R.id.konfirmasiTolak -> {
                    konfirmasiText = "tolak"
                    binding.konfirmasiJamServis.setText("")
                    binding.konfirmasiTanggalServis.setText("")
                    binding.textCat.visibility = View.GONE
                    binding.inputMekanik.visibility = View.GONE
                    binding.tvMekanik.visibility = View.GONE
                    binding.rvMekanik.visibility = View.GONE
                }
            }
        }

        binding.btnKirimKonfirmasiServis.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin mengonfirmasi servis ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!binding.konfirmasiCatatan.text.toString().equals("")){
                        if (konfirmasiText.equals("terima")) {
                            konfirmasiServis("konfirmasi_diterima")
                            Toast.makeText(this.context,"Menerima Servis pada "+binding.konfirmasiJamServis.text.toString()+" "+binding.konfirmasiTanggalServis.text.toString(), Toast.LENGTH_LONG).show()
                        } else {
                            konfirmasiServis("konfirmasi_ditolak")
                            Toast.makeText(this.context,"Menolak Servis", Toast.LENGTH_LONG).show()
                        }
                        val intent = Intent(v.context, ServisMainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                        thisParent.recreate()
                    } else {
                        Toast.makeText(v.context, "Tolong isi catatan terlebih dahulu!", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        binding.kodeServisKonfirmasi.setText("Kode "+thisParent.kdS)

        return v
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    private fun loadData(nama: String) {
        mekanikViewModel.loadData("load_mekanik_konfirmasi", nama).observe(requireActivity(), Observer { list ->
            adapter.setData(list)
        })
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            v.context,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                binding.konfirmasiJamServis.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            v.context,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                binding.konfirmasiTanggalServis.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun konfirmasiServis(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if(respon.equals("1")){
                    BaseApplication.notificationHelper.notifTransaksi("Admin telah mengonfirmasi servis Anda.")
                    Toast.makeText(v.context,"Berhasil mengonfirmasi Servis", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(v.context,"Gagal mengonfirmasi Servis", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "konfirmasi_diterima" -> {
                        hm.put("mode","konfirmasi_servis")
                        hm.put("kd_servis", thisParent.kdS)
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("kd_mekanik", kdMekanik)
                        hm.put("tgl_servis", binding.konfirmasiTanggalServis.text.toString()+" "+binding.konfirmasiJamServis.text.toString())
                        hm.put("catatan_transaksi", binding.konfirmasiCatatan.text.toString())
                        hm.put("status_transaksi", "Proses")
                        hm.put("kd_user", thisParent.kdU)
                    }
                    "konfirmasi_ditolak" -> {
                        hm.put("mode","konfirmasi_servis")
                        hm.put("kd_servis", thisParent.kdS)
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("tgl_servis", "0000-00-00 00:00:00")
                        hm.put("catatan_transaksi", binding.konfirmasiCatatan.text.toString())
                        hm.put("status_transaksi", "Tolak")
                        hm.put("kd_user", thisParent.kdU)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}