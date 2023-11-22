package com.example.admin_servis.View.Chat

import android.R
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.admin_servis.Adapter.AdapterChat
import com.example.admin_servis.Adapter.AdapterChatPelanggan
import com.example.admin_servis.UrlClass
import com.example.admin_servis.ViewModel.ChatViewModel
import com.example.admin_servis.databinding.ChatMainActivityBinding
import org.json.JSONArray

class ChatMainActivity : AppCompatActivity() {
    private lateinit var binding: ChatMainActivityBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: AdapterChatPelanggan

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()
    private var urlClass: UrlClass = UrlClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        adapter = AdapterChatPelanggan(ArrayList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadData(binding.etSearch.text.toString())
                true
            } else {
                false
            }
        }

        namaAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, daftarNama)
        binding.etSearch.setAdapter(namaAdapter)
        binding.etSearch.threshold = 0
    }

    override fun onStart() {
        super.onStart()
        loadData("")
        getNama()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(com.example.admin_servis.R.anim.slide_in_left, com.example.admin_servis.R.anim.slide_out_right)
    }

    private fun loadData(nm: String) {
        val topik = "Chat"
        chatViewModel.loadDataPelanggan(topik, nm).observe(this, Observer { dataList ->
            adapter.setData(dataList)
        })
    }

    private fun getNama() {
        val request = object : StringRequest(
            Request.Method.POST,urlClass.urlUser,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama"))
                }
                namaAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "get_nama")

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}