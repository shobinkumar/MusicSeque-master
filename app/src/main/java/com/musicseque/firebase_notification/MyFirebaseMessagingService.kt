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
import com.musicseque.utilities.Constants.PROFILE_TYPE
import com.musicseque.utilities.SharedPref
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.util.ArrayMap
import com.musicseque.event_manager.activity.EventDetailActivity
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var notificationManager: NotificationManager
    val CHANNEL_ID_ARTIST = "com.musicseque_artist"
    val CHANNEL_ID_VENUE_MANAGER = "com.musicseque_venue_manager"
    override fun onNewToken(token: String?) {
        Log.e("Token", token);
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        createChannel()
        sendNotification(remoteMessage)
//        if(SharedPref.getString(PROFILE_TYPE,"").equals("Venue Manager",true))
//        {
//            val data = remoteMessage?.getData()
//        }
//        else
//        {
//
//        }
//
//
//        sendNotification(remoteMessage)
//        val intent = Intent()
//        intent.action = "com.musicseque.NotificationCount"
//        intent.putExtra("notification_count", 1000)
//        intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
//        sendBroadcast(intent)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (SharedPref.getString(PROFILE_TYPE, "").equals("Venue Manager", true)) {
                val name = "MY_NOTIFICATION_VENUE_MANAGER"
                val desc = "This is description venue manager"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID_VENUE_MANAGER, name, importance).apply {
                    description = desc
                    setShowBadge(true)
                }


                notificationManager.createNotificationChannel(channel)
            } else {
                val name = "MY_NOTIFICATION_ARTIST"
                val desc = "This is description artist"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID_ARTIST, name, importance).apply {
                    description = desc
                    setShowBadge(true)
                }


                notificationManager.createNotificationChannel(channel)
            }


        }
    }


    public fun sendNotification(remoteMessage: RemoteMessage?) {

        if (SharedPref.getString(PROFILE_TYPE, "").equals("Venue Manager", true)) {

            val data = ((remoteMessage?.data as ArrayMap).valueAt(0)).toString()
            val json = JSONObject(data)
            json.getString("Message")

            val resultIntent = Intent(this, EventDetailActivity::class.java)
            resultIntent.putExtra("event_id", json.getString("Event_Id"))
            val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_VENUE_MANAGER)
            notificationBuilder.setAutoCancel(true)
            notificationBuilder.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
            notificationBuilder.setContentTitle(json.getString("Message"))
            // notificationBuilder.setContentText()
            notificationBuilder.setContentIntent(pendingIntent)
            notificationManager.notify(1, notificationBuilder.build())
        } else {


            val data = ((remoteMessage?.data as ArrayMap).valueAt(0)).toString()
            val json = JSONObject(data)


            if(json.get("Unique_Id") == "1" || json.get("Unique_Id") == "2")
            {

                val resultIntent = Intent(this, EventDetailActivity::class.java)
                resultIntent.putExtra("event_id", json.getString("Event_Id"))

                val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_ARTIST)
                notificationBuilder.setAutoCancel(true)
                notificationBuilder.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                notificationBuilder.setContentTitle(json.getString("Message"))
                // notificationBuilder.setContentText()
                notificationBuilder.setContentIntent(pendingIntent)
                notificationManager.notify(1, notificationBuilder.build())






            }

          else  if (json.get("Unique_Id") == "3" || json.get("Unique_Id") == "4" || json.get("Unique_Id") == "5" ||json.get("Unique_Id") == "6") {
                val resultIntent = Intent(this, NotificationActivity::class.java)


                val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_ARTIST)
                notificationBuilder.setAutoCancel(true)
                notificationBuilder.setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                notificationBuilder.setContentTitle(json.getString("Message"))
                // notificationBuilder.setContentText()
                notificationBuilder.setContentIntent(pendingIntent)
                notificationManager.notify(1, notificationBuilder.build())


            }


        }


    }

}