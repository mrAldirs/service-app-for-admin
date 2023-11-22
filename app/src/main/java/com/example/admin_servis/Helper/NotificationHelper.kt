package com.example.admin_servis.Helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.admin_servis.R

class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val packageName = "com.example.pelanggan_servis" // Ganti dengan package name aplikasi lain yang ingin Anda buka
        val className = "com.example.pelanggan_servis.View.Layout.MainActivity" // Ganti dengan nama lengkap package name ActivityChat
        val intent = Intent()
        intent.setClassName(packageName, className)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun notifTransaksi(pesan: String) {
        val channelId = "channel_transaksi"
        val channelName = "Channel Transaksi"
        val importance = NotificationManager.IMPORTANCE_HIGH

        createNotificationChannel(channelId, channelName, importance)

        val pendingIntent = createPendingIntent()

        val notification: Notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Admin")
            .setContentText(pesan)
            .setSmallIcon(R.drawable.icon_apk)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    fun notifChat(pesan: String) {
        val channelId = "channel_chat"
        val channelName = "Channel Chat"
        val importance = NotificationManager.IMPORTANCE_HIGH

        createNotificationChannel(channelId, channelName, importance)

        val pendingIntent = createPendingIntent()

        val notification: Notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Admin")
            .setContentText(pesan)
            .setSmallIcon(R.drawable.icon_apk)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(2, notification)
    }
}
