package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.AkunModel
import com.example.admin_servis.Model.UserModel
import org.json.JSONArray
import org.json.JSONObject

class UserRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    data class DetailData(
        val userModel: UserModel,
        val akunModel: AkunModel
    )

    fun loadData(nama: String): LiveData<List<UserModel>> {
        val data = MutableLiveData<List<UserModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlUser,
            Response.Listener { response ->
                val dataList = mutableListOf<UserModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = UserModel(
                        jsonObject.getString("kd_user"),
                        jsonObject.getString("nama"), "", "", "",
                        jsonObject.getString("no_hp"),
                        jsonObject.getString("profil")
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "show_data_user")
                hm.put("nama", nama)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun detail(kd: String): LiveData<DetailData> {
        val data = MutableLiveData<DetailData>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlUser,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("kd_user")
                val st2 = jsonObject.getString("nama")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("email")
                val st5 = jsonObject.getString("usia")
                val st6 = jsonObject.getString("no_hp")
                val st7 = jsonObject.getString("profil")
                val st8 = jsonObject.getString("kd_akun")
                val st9 = jsonObject.getString("username")
                val st10 = jsonObject.getString("password")
                val st11 = jsonObject.getString("sts_akun")

                val dataList = UserModel(st1,st2,st3,st4,st5,st6,st7)
                val dataList2 = AkunModel(st8, st9, st10, "", "",st11)

                val detailData = DetailData(dataList, dataList2)
                data.value = detailData
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("kd_user", kd)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }
}