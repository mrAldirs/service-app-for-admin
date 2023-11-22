package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.ServisLineModel
import com.example.admin_servis.Model.ServisModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ServisRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun lineChart(): LiveData<List<ServisLineModel>> {
        val transaksiData = MutableLiveData<List<ServisLineModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlServis,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val transaksiList = mutableListOf<ServisLineModel>()

                    var servisByDate: ServisLineModel? = null
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val xValue = jsonObject.getString("x")
                        val selesai = jsonObject.getString("y1")?.toFloatOrNull()
                        val ditolak = jsonObject.getString("y2")?.toFloatOrNull()

                        // Jika ada entri sebelumnya, tambahkan ke transaksiList jika tidak ada nilai null
                        if (servisByDate != null) {
                            if (servisByDate.selesaiTotal != null ||
                                servisByDate.ditolakTotal != null
                            ) {
                                transaksiList.add(servisByDate)
                            }
                        }

                        servisByDate = ServisLineModel(xValue,
                            selesai?.toFloat(), ditolak?.toFloat())
                    }

                    // Tambahkan entri terakhir ke transaksiList jika tidak ada nilai null
                    if (servisByDate != null) {
                        if (servisByDate.selesaiTotal != null ||
                            servisByDate.ditolakTotal != null
                        ) {
                            transaksiList.add(servisByDate)
                        }
                    }

                    transaksiData.value = transaksiList
                } catch (e: JSONException) {
                    // Tangani error parsing JSON
                }
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["mode"] = "line_chart"

                return params
            }
        }

        requestQueue.add(request)
        return transaksiData
    }

    fun loadData(
        mode: String,
        nama: String,
        callback: (Boolean, List<ServisModel>?) -> Unit // Updated callback parameter
    ) {
        val request = object : StringRequest(
            Method.POST, baseUrl.urlServis,
            Response.Listener { response ->
                val dataList = mutableListOf<ServisModel>()
                val jsonArray = JSONArray(response)
                if (jsonArray.length() == 0) {
                    callback(true, null) // Data is empty
                } else {
                    for (x in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(x)
                        val item = ServisModel(
                            jsonObject.getString("kd_transaksi"),
                            jsonObject.getString("tgl_transaksi"),
                            jsonObject.getString("status_transaksi"), "", "",
                            jsonObject.getString("kerusakan"), "", "", "", "",
                            jsonObject.getString("nama"), "", ""
                        )
                        dataList.add(item)
                    }
                    callback(false, dataList) // Data is available
                }
            },
            Response.ErrorListener { error ->
                callback(true, null) // Error occurred
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm["mode"] = mode
                hm["nama"] = nama

                return hm
            }
        }
        requestQueue.add(request)
    }

    fun batalkan(kd:String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlServis,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "batalkan")
                hm.put("kd_transaksi", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}