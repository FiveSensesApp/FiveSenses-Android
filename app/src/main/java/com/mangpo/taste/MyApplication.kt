package com.mangpo.taste

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    companion object {
        lateinit var spf: SharedPreferences
        lateinit var encryptedSpf: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        spf = applicationContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        encryptedSpf = EncryptedSharedPreferences.create(
            "${getString(R.string.app_name)}_encrypted",
            mainKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        createChannel()
    }

    private fun createChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.getNotificationChannel("5GAAM_CHANNEL_ID")

        if (channel==null) {
            val name = "5gaam"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("5GAAM_CHANNEL_ID", name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}