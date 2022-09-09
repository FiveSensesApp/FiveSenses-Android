package com.mangpo.taste

import android.app.Application
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
            getString(R.string.app_name),
            mainKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}