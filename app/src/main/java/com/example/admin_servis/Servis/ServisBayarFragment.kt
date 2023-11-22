package com.example.admin_servis.Servis

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Helper.BaseApplication
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.example.admin_servis.Helper.PhotoHelper
import com.example.admin_servis.View.Servis.ServisDetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.yigitserin.currencyedittext.CurrencyEditText
import kotlinx.android.synthetic.main.servis_bayar_fragment.view.*
import org.json.JSONObject
import java.util.Calendar
import java.util.Locale

class ServisBayarFragment : BottomSheetDialogFragment() {

    lateinit var thisParent : ServisDetailActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var imStr = ""
    var fileUri = Uri.parse("")

    val pembayaranSp = arrayOf("--Pilih Metode Pembayaran--","COD")
    lateinit var adapterPembayaran: ArrayAdapter<String>

    var bOngkir : Int = 0
    var bAdmin : Int = 0
    var bHarga : Int = 0
    var bTotal : Int = 0

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as ServisDetailActivity
        v = layoutInflater.inflate(R.layout.servis_bayar_fragment, container, false)

        photoHelper = PhotoHelper()
        urlClass = UrlClass()

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val currencyList = listOf(v.bayarServisTotal, v.bayarServisAdmin, v.bayarServisOngkir, v.bayarServisTotalPembayaran)
        setupCurrency(currencyList)

        v.btnChoosePhotoPembayaranServis.setOnClickListener {
            requestPermission()
        }

        adapterPembayaran = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,pembayaranSp)
        v.spPembayaran.adapter = adapterPembayaran

        v.btnBatalkan.setOnClickListener {
            dismiss()
        }

        v.btnTutup.setOnClickListener {
            dismiss()
        }

        v.bayarServisTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        v.bayarServisJam.setOnClickListener {
            showTimePickerDialog()
        }

        v.kodeTransaksiBayar.setText("Kode "+thisParent.kdS)

        v.btnTotal.setOnClickListener {
            val tot = v.bayarServisTotal.text.toString().replace(",", "").toLong()
            val adm = v.bayarServisAdmin.text.toString().replace(",", "").toLong()
            val ong = v.bayarServisOngkir.text.toString().replace(",", "").toLong()
            val result = tot + adm + ong

            v.bayarServisTotalPembayaran.setText(result.toString())

            bHarga = tot.toInt()
            bAdmin = adm.toInt()
            bTotal = result.toInt()
        }

        v.btnKirimKBayarServis.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Peringatan!")
                .setMessage("Apakah Anda ingin mengonfirmasi pembayaran servis?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (!v.bayarServisJam.text.toString().equals("") && !v.bayarServisTanggal.text.toString().equals("") && !v.bayarServisTotal.text.toString().equals("")
                        && !v.bayarServisAdmin.text.toString().equals("") && !v.bayarServisOngkir.text.toString().equals("") && !v.bayarServisTotalPembayaran.text.toString().equals("")){
                        bayarServis("bayar_servis")
                        val intent = Intent(v.context, ServisMainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                    } else {
                        Toast.makeText(v.context, "Tolong lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    private fun setupCurrency(currencyEditTextList: List<CurrencyEditText>) {
        val locale = Locale("id", "ID")
        val decimalDigits = 2

        for (currencyEditText in currencyEditTextList) {
            currencyEditText.locale = locale
            currencyEditText.decimalDigits = decimalDigits
        }
    }

    override fun onStart() {
        super.onStart()
        ongkir()
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        imStr = photoHelper.getBitMapToString(v.imgPembayaranServis, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            v.context,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                v.bayarServisJam.setText(time)
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
                v.bayarServisTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun ongkir() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlOngkir,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val ong = jsonObject.getString("ongkir")

                v.bayarServisOngkir.setText(ong)
                bOngkir = ong.replace(",", "").toInt()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("kd_user", thisParent.kdU)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun bayarServis(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if(respon.equals("1")){
                    BaseApplication.notificationHelper.notifTransaksi("Anda telah melakukan pembayaran servis. Lihat struk pada riwayat pembayaran Anda!")
                    Toast.makeText(v.context,"Berhasil melakukan pembayaran Servis", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(v.context,"Gagal melakukan pembayaran Servis", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "bayar_servis" -> {
                        hm.put("mode","bayar_servis")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("tgl_pembayaran", v.bayarServisTanggal.text.toString()+" "+v.bayarServisJam.text.toString())
                        hm.put("rek_payment", v.spPembayaran.selectedItem.toString())
                        hm.put("bayar", bHarga.toString())
                        hm.put("biaya_admin", bAdmin.toString())
                        hm.put("ongkir", bOngkir.toString())
                        hm.put("total_bayar", bTotal.toString())
                        hm.put("status_pembayaran", "Selesai")
                        hm.put("kd_user", thisParent.kdU)
                        hm.put("image",imStr)
                        hm.put("file",namaFile)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}