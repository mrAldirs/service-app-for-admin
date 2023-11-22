package com.example.admin_servis

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.login_activity.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var urlClass: UrlClass

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()

        progressBar.progress = 50
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 500 // 1 second
        animator.start()

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            validationAccount("login")
        }
    }

    private fun validationAccount(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val level = jsonObject.getString("level")
                val statusAkun = jsonObject.getString("sts_akun")
                if(level.equals("Admin") && statusAkun.equals("AKTIF")){
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val nama = jsonObject.getString("nama")
                    val user = jsonObject.getString("kd_user")
                    val username = jsonObject.getString("username")
                    val password = jsonObject.getString("password")
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,nama)
                    prefEditor.putString(USER,user)
                    prefEditor.putString(USERNAME, username)
                    prefEditor.putString(PASSWORD, password)
                    prefEditor.commit()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan!")
                        .setMessage("Username atau Katasandi Anda salah!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            progressBar.visibility = View.GONE
                        })
                        .show()
                }
            },
            Response.ErrorListener { error ->
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Peringatan!")
                    .setIcon(R.drawable.warning)
                    .setMessage("Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("username",etUsername.text.toString())
                hm.put("password",etPassword.text.toString())
                when(mode) {
                    "login" -> {
                        hm.put("mode", "login")
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}