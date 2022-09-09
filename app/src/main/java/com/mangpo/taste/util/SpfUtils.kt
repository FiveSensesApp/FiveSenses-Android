package com.mangpo.taste.util

import com.mangpo.taste.MyApplication.Companion.encryptedSpf
import com.mangpo.taste.MyApplication.Companion.spf

object SpfUtils {
    fun writeSpf(key: String, value: Boolean) {
        with(spf.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getSpf(key: String, defaultValue: Boolean): Boolean? {
        return spf.getBoolean(key, defaultValue)
    }

    fun writeEncryptedSpf(key: String, value: String) {
        with(encryptedSpf.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getEncryptedSpf(key: String): String? {
        return encryptedSpf.getString(key, null)
    }

    fun clearEncryptedSpf() {
        with(encryptedSpf.edit()) {
            putString("jwt", null)
            apply()
        }
    }
}