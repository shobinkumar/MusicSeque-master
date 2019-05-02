package com.musicseque.firebase_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var notificationManager:NotificationManager
    val CHANNEL_ID="com.musicseque"
    override fun onNewToken(token: String?) {
        Log.e("Token", token);
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

         notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()
        sendNotification(remoteMessage)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "MY_NOTIFICATION"
            val desc = "This is description"
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel=NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=desc
                setShowBadge(true)
            }


            notificationManager.createNotificationChannel(channel)

        }
    }


    public fun sendNotification(remoteMessage: RemoteMessage?) {
        val notificationBuilder=NotificationCompat.Builder(this,CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
        notificationBuilder.setContentTitle("KIda j")
        notificationManager.notify(1,notificationBuilder.build())
    }

}