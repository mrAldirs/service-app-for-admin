package com.example.admin_servis.View.Chat

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_servis.Adapter.AdapterChat
import com.example.admin_servis.Helper.BaseApplication
import com.example.admin_servis.Model.ChatModel
import com.example.admin_servis.R
import com.example.admin_servis.ViewModel.ChatViewModel
import com.example.admin_servis.databinding.ChatActivityBinding
import com.example.admin_servis.Helper.PhotoHelper
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ChatActivityBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var adapter: AdapterChat
    private var topik = ""

    lateinit var photoHelper: PhotoHelper
    var namaFile = ""
    var imStr = ""
    var fileUri = Uri.parse("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        photoHelper = PhotoHelper()
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        try {
            val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.btnAmbilGambar.setOnClickListener {
            requestPermission()
        }

        adapter = AdapterChat(ArrayList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        topik = intent?.getStringExtra("topik").toString()
        binding.chatTopik.text = "Topik : $topik"

        binding.btnKirim.setOnClickListener {
            insert()
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun requestPermission() = runWithPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA ) {
        fileUri = photoHelper.getOutputMediaFileUri()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, photoHelper.getRcCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            photoHelper.getRcCamera() -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        binding.cardImage.visibility = View.VISIBLE
                        imStr = photoHelper.getBitMapToString(binding.chatImg, fileUri)
                        namaFile = photoHelper.getMyFileName()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        // kode untuk kondisi kedua jika dibatalkan
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun loadData() {
        val kode = intent?.getStringExtra("kode").toString()
        chatViewModel.loadData(topik, "chat$kode").observe(this, Observer { dataList ->
            adapter.setData(dataList)
        })
    }

    private  fun insert() {
        val kode = intent?.getStringExtra("kode").toString()
        val data = ChatModel(
            "",
            "USR000001",
            topik,
            binding.chatPesan.text.toString(),
            "",
            imStr, "chat$kode", ""
        )

        chatViewModel.insert(data, namaFile).observe(this, Observer { respon ->
            loadData()
            BaseApplication.notificationHelper.notifChat(binding.chatPesan.text.toString())
            binding.chatPesan.clearFocus()
            binding.chatPesan.setText("")
            binding.cardImage.visibility = View.GONE
        })
    }

    fun delete(kd: String) {
        chatViewModel.delete(kd).observe(this, Observer {
            Toast.makeText(this, "Berhasil menghapus pesan!", Toast.LENGTH_SHORT).show()
            loadData()
        })
    }
}