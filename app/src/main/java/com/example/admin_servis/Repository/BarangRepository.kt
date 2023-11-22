package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.BarangModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BarangRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(mode: String, nama: String): LiveData<List<BarangModel>> {
        val data = MutableLiveData<List<BarangModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKatalog,
            Response.Listener { response ->
                val dataList = mutableListOf<BarangModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val item = BarangModel(
                        jsonObject.getString("kd_barang"),
                        jsonObject.getString("kd_katalog"),
                        jsonObject.getString("status_katalog"),
                        jsonObject.getString("tgl_upload"),
                        jsonObject.getString("nama_barang"),
                        jsonObject.getString("jenis_barang"), "", "", "",
                        jsonObject.getString("img_barang")
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

    fun detail(kd: String): LiveData<BarangModel> {
        val data = MutableLiveData<BarangModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_barang")
                val st2 = jsonObject.getString("kd_katalog")
                val st3 = jsonObject.getString("status_katalog")
                val st4 = jsonObject.getString("tgl_upload")
                val st5 = jsonObject.getString("nama_barang")
                val st6 = jsonObject.getString("jenis_barang")
                val st7 = jsonObject.getString("harga_katalog")
                val st8 = jsonObject.getString("spesifikasi")
                val st9 = jsonObject.getString("ket_barang")
                val st10 = jsonObject.getString("img_barang")

                val dataList = BarangModel(st1,st2,st3,st4,st5,st6,st7,st8,st9,st10)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_barang", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun delete(kd: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "delete")
                hm.put("kd_barang", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }

    fun insert(barang: BarangModel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                var namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                val hm = HashMap<String, String>()
                hm.put("mode","insert")
                hm.put("nama_barang", barang.nama_barang)
                hm.put("jenis_barang", barang.jenis_barang)
                hm.put("spesifikasi", barang.spesifikasi)
                hm.put("harga_katalog", barang.harga_katalog)
                hm.put("ket_barang", barang.ket_barang)
                hm.put("image", barang.img_barang)
                hm.put("file", namaFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun edit(kd: String, barang: BarangModel) : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlKatalog,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                var namaFile = "IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                val hm = HashMap<String, String>()
                hm.put("mode","edit")
                hm.put("kd_barang", kd)
                hm.put("kd_katalog", barang.kd_katalog)
                hm.put("nama_barang", barang.nama_barang)
                hm.put("jenis_barang", barang.jenis_barang)
                hm.put("spesifikasi", barang.spesifikasi)
                hm.put("harga_katalog", barang.harga_katalog)
                hm.put("ket_barang", barang.ket_barang)
                hm.put("image", barang.img_barang)
                hm.put("file", namaFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }
}