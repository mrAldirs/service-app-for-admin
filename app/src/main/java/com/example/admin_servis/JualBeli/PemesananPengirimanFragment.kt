package com.example.admin_servis.JualBeli

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.R
import com.example.admin_servis.UrlClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.pemesanan_pengiriman_fragment.view.*
import org.json.JSONObject
import java.util.Calendar

class PemesananPengirimanFragment : BottomSheetDialogFragment() {

    lateinit var thisParent : PemesananDetailActivity
    lateinit var v : View
    lateinit var urlClass : UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PemesananDetailActivity
        v = inflater.inflate(R.layout.pemesanan_pengiriman_fragment, container, false)

        urlClass = UrlClass()
        val bundle = arguments
        v.kirimOrderKode.setText("Kode Transaksi : "+bundle?.getString("kode").toString())
        v.kirimOrderPayment.setText(bundle?.getString("pay").toString())

        v.btnBatalkan.setOnClickListener {dismiss() }

        v.btnTutup.setOnClickListener { dismiss() }

        v.kirimOrderJam.setOnClickListener {
            showTimePickerDialog()
        }

        v.kirimOrderTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        v.btnSubmitPengirimOrder.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Informasi!")
                .setMessage("Apakah Anda ingin mengonfirmasi pengiriman barang?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (v.kirimOrderPayment.text.toString().equals("COD")) {
                        AlertDialog.Builder(v.context)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan!")
                            .setMessage("Order ini menggunakan Sistem COD, tekan Ya jika Anda mengonfirmasi pembayaran ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                                pengirimanOrder("pengiriman_order")
                                Toast.makeText(thisParent, "Sukses mengonfirmasi pengiriman barang dengan sistem pembayaran COD!", Toast.LENGTH_SHORT).show()
                                requireActivity().recreate()
                                dismiss()
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                        true
                    } else {
                        pengirimanOrder("pengiriman_order")
                        Toast.makeText(thisParent, "Sukses mengonfirmasi pengiriman barang!", Toast.LENGTH_SHORT).show()
                        requireActivity().recreate()
                        dismiss()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
            true
        }

        return v
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            v.context,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                v.kirimOrderJam.setText(time)
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
                v.kirimOrderTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun pengirimanOrder(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlTransaksi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if(respon.equals("1")){
//                    Toast.makeText(thisParent,"Berhasil mengonfirmasi Order", Toast.LENGTH_LONG).show()
//                } else if(respon.equals("2")){
//                    Toast.makeText(thisParent,"Berhasil mengonfirmasi Order", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(thisParent,"Gagal mengonfirmasi Order", Toast.LENGTH_LONG).show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "pengiriman_order" -> {
                        hm.put("mode", "pengiriman_order")
                        hm.put("kd_jadwal", thisParent.kdJ)
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("kd_user", thisParent.kdU)
                        hm.put("payment", v.kirimOrderPayment.text.toString())
                        hm.put("tglkirim_jadwal", v.kirimOrderTanggal.text.toString() + " " +v.kirimOrderJam.text.toString())
                        hm.put("nama_pengirim", v.kirimOrderPengirim.text.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}