package com.musicseque.firebase_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String?) {
        Log.e("Token", token);
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {


        createChannel()
        sendNotification(remoteMessage)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID="com.musicseque"
            val name = "MY_NOTIFICATION"
            val desc = "This is description"
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel=NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=desc
                setShowBadge(true)
            }

            val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }


    public fun sendNotification(remoteMessage: RemoteMessage?) {

    }

}