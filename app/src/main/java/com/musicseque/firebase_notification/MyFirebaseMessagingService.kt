package com.musicseque.firebase_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var notificationManager: NotificationManager
    val CHANNEL_ID = "com.musicseque"
    override fun onNewToken(token: String?) {
        Log.e("Token", token);
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()
        sendNotification(remoteMessage)
        val intent = Intent()
        intent.action = "com.musicseque.NotificationCount"
        intent.putExtra("notification_count", 1000)
        intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        sendBroadcast(intent)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "MY_NOTIFICATION"
            val desc = "This is description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = desc
                setShowBadge(true)
            }


            notificationManager.createNotificationChannel(channel)

        }
    }


    public fun sendNotification(remoteMessage: RemoteMessage?) {
        val resultIntent = Intent(this, NotificationActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
        notificationBuilder.setContentTitle("Kida j")
        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(1, notificationBuilder.build())
    }

}