package com.mangpo.taste.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkConnection @Inject constructor(@ApplicationContext private val appContext: Context): LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()

        updateConnection()
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
    }

    override fun onInactive() {
        super.onInactive()

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                postValue(false)
            }
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

        }

        return networkCallback
    }


    private fun updateConnection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            postValue(connectivityManager.isDefaultNetworkActive)
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            postValue(activeNetwork?.isConnected)
        }
    }

}