package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.ChatModel
import org.json.JSONArray
import org.json.JSONObject

class ChatRepository (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun loadData(topik: String, kdParent: String): LiveData<List<ChatModel>> {
        val data = MutableLiveData<List<ChatModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlChat,
            Response.Listener { response ->
                val dataList = mutableListOf<ChatModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = ChatModel(
                        jsonObject.getString("kd_chat"),
                        jsonObject.getString("nama"),
                        jsonObject.getString("topik_chat"),
                        jsonObject.getString("teks_chat"),
                        jsonObject.getString("tgl_chat"),
                        jsonObject.getString("gambar_chat"),
                        jsonObject.getString("kode_parent"),
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
                hm.put("mode", "load_data")
                hm.put("topik_chat", topik)
                hm.put("kode_parent", kdParent)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun loadDataPelanggan(topik: String, nm: String): LiveData<List<ChatModel>> {
        val data = MutableLiveData<List<ChatModel>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlChat,
            Response.Listener { response ->
                val dataList = mutableListOf<ChatModel>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = ChatModel(
                        jsonObject.getString("kd_user"),
                        jsonObject.getString("nama"),
                        jsonObject.getString("topik_chat"),
                        jsonObject.getString("teks_chat"),
                        jsonObject.getString("tgl_chat"),
                        jsonObject.getString("gambar_chat"),
                        jsonObject.getString("kode_parent"),
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
                hm.put("mode", "load_data_pelanggan")
                hm.put("topik_chat", topik)
                hm.put("nama", nm)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun insert(chat: ChatModel, nmFile: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlChat,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()

                hm.put("mode", "insert")
                hm.put("teks_chat", chat.teks_chat)
                hm.put("kd_user", chat.nama)
                hm.put("topik_chat", chat.topik_chat)
                hm.put("teks_chat", chat.teks_chat)
                hm.put("kode_parent", chat.kode_parent)
                hm.put("image", chat.gambar_chat)
                hm.put("file", nmFile)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun delete(kd: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlChat,
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
                hm.put("kd_chat", kd)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}