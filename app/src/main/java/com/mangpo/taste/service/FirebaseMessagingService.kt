package com.mangpo.taste.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mangpo.taste.R
import com.mangpo.taste.view.MainActivity

class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendDataMessage(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!)
    }

    private fun sendDataMessage(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val style = NotificationCompat.BigTextStyle()
        style.setBigContentTitle(title)
        style.bigText(body)

        val notificationBuilder = NotificationCompat.Builder(this, "5GAAM_CHANNEL_ID")
        notificationBuilder.setContentTitle(title)
            .setSmallIcon(R.drawable.noti)
            .setContentText(body)
            .setStyle(style)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(100, notificationBuilder.build() )
    }
}