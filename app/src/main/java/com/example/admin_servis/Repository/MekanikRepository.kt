package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.MekanikModel
import org.json.JSONArray
import org.json.JSONObject

class MekanikRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(mode: String, nm: String): LiveData<List<MekanikModel>> {
        val data = MutableLiveData<List<MekanikModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMekanik,
            Response.Listener { response ->
                val dataList = mutableListOf<MekanikModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = MekanikModel(jsonObject.getString("kd_mekanik"),
                        jsonObject.getString("nama_mekanik"),
                        jsonObject.getString("foto_mekanik"),
                        jsonObject.getString("status_mekanik"),
                        jsonObject.getString("average_rating")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", mode)
                hm.put("nama_mekanik", nm)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun detail(kd: String): LiveData<MekanikModel> {
        val data = MutableLiveData<MekanikModel>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMekanik,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_mekanik")
                val st2 = jsonObject.getString("nama_mekanik")
                val st3 = jsonObject.getString("foto_mekanik")
                val st4 = jsonObject.getString("status_mekanik")
                val st5 = jsonObject.getString("average_rating")

                val dataList = MekanikModel(st1,st2,st3,st4,st5)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "mekanik_servis")
                hm.put("kd_servis", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}