package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.PengirimanModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PengirimanRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(mode: String, nama: String): LiveData<List<PengirimanModel>> {
        val data = MutableLiveData<List<PengirimanModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlPengiriman,
            Response.Listener { response ->
                val dataList = mutableListOf<PengirimanModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val item = PengirimanModel(
                        jsonObject.getString("kd_jadwal"), "",
                        jsonObject.getString("nama"), "",
                        jsonObject.getString("alamat"),"",
                        jsonObject.getString("nama_barang"),"","",
                        jsonObject.getString("tglkirim_jadwal"),"",
                        jsonObject.getString("tglsampai_jadwal"),""
                    )
                    dataList.add(item)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", mode)
                hm.put("nama_barang", nama)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun detail(kd: String): LiveData<PengirimanModel> {
        val data = MutableLiveData<PengirimanModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlPengiriman,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_jadwal")
                val st2 = jsonObject.getString("nama")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("kd_transaksi")
                val st5 = jsonObject.getString("nama_barang")
                val st6 = jsonObject.getString("tglkirim_jadwal")
                val st7 = jsonObject.getString("nama_pengirim")
                val st8 = jsonObject.getString("bukti_kirim")
                val st9 = jsonObject.getString("tglsampai_jadwal")
                val st10 = jsonObject.getString("kd_user")
                val st11 = jsonObject.getString("status_pembayaran")

                val dataList = PengirimanModel(
                    st1,st10, st2, st11, st3, st4, st5, "","", st6, st7, st9, st8)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_jadwal", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun bukti(mode: String, kd: String, kirim: PengirimanModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlPengiriman,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                var namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                hm.put("mode", mode)
                hm.put("kd_jadwal", kd)
                hm.put("image", kirim.bukti_kirim)
                hm.put("file", namaFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }
}