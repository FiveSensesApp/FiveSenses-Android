package com.mangpo.taste.util

import com.mangpo.taste.MyApplication.Companion.encryptedSpf
import com.mangpo.taste.MyApplication.Companion.spf

object SpfUtils {
    fun writeSpf(key: String, value: String) {
        with(spf.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun writeSpf(key: String, value: Int) {
        with(spf.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun writeSpf(key: String, value: Boolean) {
        with(spf.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getStrSpf(key: String): String? {
        return spf.getString(key, null)
    }

    fun getBooleanSpf(key: String, defaultValue: Boolean): Boolean {
        return spf.getBoolean(key, defaultValue)
    }

    fun writeEncryptedSpf(key: String, value: String) {
        with(encryptedSpf.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getStrEncryptedSpf(key: String): String? {
        return encryptedSpf.getString(key, null)
    }

    fun writeEncryptedSpf(key: String, value: Int) {
        with(encryptedSpf.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getIntEncryptedSpf(key: String): Int {
        return encryptedSpf.getInt(key, -1)
    }

    fun clear() {
        spf.edit().clear().apply()
        encryptedSpf.edit().clear().apply()
    }
}