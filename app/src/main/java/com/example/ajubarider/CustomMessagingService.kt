@file:Suppress("DEPRECATION")

package com.example.ajubarider

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CustomMessagingService: FirebaseMessagingService() {

    lateinit var notificationManager: NotificationManager
    lateinit var notification:Notification

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if(p0.notification!=null){
            var title= p0.notification!!.title
            var message= p0.notification!!.body
            generateNotification(title!!,message!!)

        }
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this@CustomMessagingService, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this@CustomMessagingService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var audioAttributes = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_notification_id"
            val CHANNEL_NAME = BuildConfig.APPLICATION_ID + "_notification_name"
            var notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(true)
            notificationChannel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))

            notificationChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    +"://"+packageName+"/raw/alert.mp3"),audioAttributes)
            notificationManager.createNotificationChannel(notificationChannel)
            var builder : NotificationCompat.Builder = NotificationCompat.Builder(this,CHANNEL_ID)
            builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
            notification = builder.build()
            startForeground(0,notification)

        }
        else{
            var nb : Notification.Builder= Notification.Builder(this)
            nb.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            +"://"+packageName+"/raw/alert.mp3"),audioAttributes)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .build()
            notification= nb.notification
            notification.flags= Notification.FLAG_AUTO_CANCEL
            notificationManager.notify(0,notification)
        }




    }
}