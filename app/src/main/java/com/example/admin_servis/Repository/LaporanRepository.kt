package com.example.admin_servis.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.GlobalData.BaseUrl
import com.example.admin_servis.Model.LabaRugi
import com.example.admin_servis.Model.LaporanModel
import com.example.admin_servis.Model.TransaksiMain
import com.example.admin_servis.Model.UserTransaksiMain
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LaporanRepository  (context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl: BaseUrl = BaseUrl

    fun laporan(callback: (List<LaporanModel>?, String?) -> Unit) {
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMain,
            Response.Listener { response ->
                val laporanList = mutableListOf<LaporanModel>()
                val jsonArray = JSONArray(response)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val jenisTransaksi = jsonObject.getString("jenisTransaksi")
                    val laporan = jsonObject.getString("laporan")
                    val laporanItem = LaporanModel(jenisTransaksi, laporan, "", "")
                    laporanList.add(laporanItem)
                }
                callback(laporanList, null)
            },
            Response.ErrorListener { error ->
                callback(null, error.message)
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("mode", "laporan")
                return params
            }
        }

        requestQueue.add(request)
    }

    fun pendapatan(callback: (LaporanModel?, String?) -> Unit): (LaporanModel?, String?) -> Unit {
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMain,
            Response.Listener { response ->
                try {
                    if (response == "0") {
                        callback(null, "No data found")
                    } else {
                        val jsonArray = JSONArray(response)
                        val dataList = mutableListOf<LaporanModel>()
                        for (x in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(x)
                            val data = LaporanModel("", "",
                                jsonObject.getString("total"),""
                            )
                            dataList.add(data)
                        }
                        callback(dataList[0], null)
                    }
                } catch (e: JSONException) {
                    callback(null, "Error parsing JSON")
                }
            },
            Response.ErrorListener { error ->
                callback(null, error.message)
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("mode", "pendapatan")
                return params
            }
        }

        requestQueue.add(request)
        return callback
    }

    fun detailPendapatan(thn: String): LiveData<List<LabaRugi>> {
        val data = MutableLiveData<List<LabaRugi>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMain,
            Response.Listener { response ->
                val dataList = mutableListOf<LabaRugi>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val item = LabaRugi(
                        jsonObject.getString("bulan"),
                        jsonObject.getString("total_pendapatan"),
                        jsonObject.getString("total_pengeluaran"),
                        jsonObject.getString("laba")
                    )
                    dataList.add(item)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail_pendapatan")
                hm.put("tahun", thn)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun transaksiDetail(): MutableLiveData<List<UserTransaksiMain>> {
        val transaksiData = MutableLiveData<List<UserTransaksiMain>>()
        val request = object : StringRequest(
            Method.POST, baseUrl.urlMain,
            Response.Listener { response ->
                val transactionList = mutableListOf<UserTransaksiMain>()

                val jsonObject = JSONObject(response)
                val keys = jsonObject.keys()

                while (keys.hasNext()) {
                    val nama = keys.next()
                    val transactionsObject = jsonObject.getJSONObject(nama)
                    val transactions = mutableMapOf<String, String>()

                    val transaksiKeys = transactionsObject.keys()
                    while (transaksiKeys.hasNext()) {
                        val jenisTransaksi = transaksiKeys.next()
                        val jumlahTransaksi = transactionsObject.getString(jenisTransaksi)
                        transactions[jenisTransaksi] = jumlahTransaksi
                    }

                    val userTransaction = UserTransaksiMain(nama, transactions)
                    transactionList.add(userTransaction)
                }

                transaksiData.value = transactionList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm["mode"] = "detail_pie_chart"

                return hm
            }
        }
        requestQueue.add(request)
        return transaksiData
    }
}