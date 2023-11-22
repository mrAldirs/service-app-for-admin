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
import kotlinx.android.synthetic.main.jual_pengiriman_fragment.view.*
import org.json.JSONObject
import java.util.Calendar

class JualPengirimanFragment : BottomSheetDialogFragment() {

    lateinit var thisParent: JualDetailActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as JualDetailActivity
        v = inflater.inflate(R.layout.jual_pengiriman_fragment, container, false)

        urlClass = UrlClass()
        val bundle = arguments
        v.kirimJualKode.setText("Kode Transaksi : "+bundle?.getString("kode").toString())
        v.kirimJualPayment.setText(bundle?.getString("pay").toString())

        v.btnBatalkan.setOnClickListener { dismiss() }

        v.btnTutup.setOnClickListener { dismiss() }


        v.kirimJualJam.setOnClickListener {
            showTimePickerDialog()
        }

        v.kirimJualTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        v.btnSubmitPengirimJual.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setIcon(R.drawable.warning)
                .setTitle("Informasi!")
                .setMessage("Apakah Anda ingin mengonfirmasi pengiriman barang?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                    if (v.kirimJualPayment.text.toString().equals("COD")) {
                        AlertDialog.Builder(v.context)
                            .setIcon(R.drawable.warning)
                            .setTitle("Peringatan!")
                            .setMessage("Pembelian ini menggunakan Sistem COD, tekan Ya jika Anda mengonfirmasi pembayaran ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                                pengiriman("pengiriman_jual")
                                Toast.makeText(thisParent, "Sukses mengonfirmasi pengiriman barang dengan sistem pembayaran COD!", Toast.LENGTH_SHORT).show()
                                requireActivity().recreate()
                                dismiss()
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                        true
                    } else {
                        pengiriman("pengiriman_jual")
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
                v.kirimJualJam.setText(time)
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
                v.kirimJualTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun pengiriman(mode: String) {
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
                    "pengiriman_jual" -> {
                        hm.put("mode", "pengiriman_jual")
                        hm.put("kd_transaksi", thisParent.kdT)
                        hm.put("kd_pembayaran", thisParent.kdP)
                        hm.put("kd_user", thisParent.kdU)
                        hm.put("kd_barang", thisParent.kdB)
                        hm.put("payment", v.kirimJualPayment.text.toString())
                        hm.put("tglkirim_jadwal", v.kirimJualTanggal.text.toString() + " " +v.kirimJualJam.text.toString())
                        hm.put("nama_pengirim", v.kirimJualPengirim.text.toString())
                    }
                }
                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}