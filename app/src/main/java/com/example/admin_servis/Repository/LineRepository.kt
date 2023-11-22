package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.LineModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LineRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun detail(): LiveData<List<LineModel>> {
        val transaksiData = MutableLiveData<List<LineModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMain,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val transaksiList = mutableListOf<LineModel>()

                    var transaksiByDate: LineModel? = null
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val jenisTransaksi = jsonObject.getString("x")
                        val beliTotal = jsonObject.getString("y1")?.toFloatOrNull()
                        val jualTotal = jsonObject.getString("y2")?.toFloatOrNull()
                        val servisTotal = jsonObject.getString("y3")?.toFloatOrNull()

                        // Jika ada entri sebelumnya, tambahkan ke transaksiList jika tidak ada nilai null
                        if (transaksiByDate != null) {
                            if (transaksiByDate.beliTotal != null ||
                                transaksiByDate.jualTotal != null ||
                                transaksiByDate.servisTotal != null
                            ) {
                                transaksiList.add(transaksiByDate)
                            }
                        }

                        transaksiByDate = LineModel(jenisTransaksi,
                            beliTotal?.toFloat(), jualTotal?.toFloat(), servisTotal?.toFloat())
                    }

                    // Tambahkan entri terakhir ke transaksiList jika tidak ada nilai null
                    if (transaksiByDate != null) {
                        if (transaksiByDate.beliTotal != null ||
                            transaksiByDate.jualTotal != null ||
                            transaksiByDate.servisTotal != null
                        ) {
                            transaksiList.add(transaksiByDate)
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

}