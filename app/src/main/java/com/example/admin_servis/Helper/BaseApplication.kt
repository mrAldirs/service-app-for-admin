package com.example.admin_servis.Helper

import android.app.Application

class BaseApplication : Application() {

    companion object {
        lateinit var notificationHelper: NotificationHelper
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(applicationContext)
    }
}

