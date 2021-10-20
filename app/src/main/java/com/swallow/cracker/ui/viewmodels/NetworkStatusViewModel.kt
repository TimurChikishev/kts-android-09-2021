package com.swallow.cracker.ui.viewmodels

import android.app.Application
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkStatusViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val noNetworkFlow = MutableStateFlow(isNoNetwork())

    val isNoNetwork: Flow<Boolean>
        get() = noNetworkFlow.asStateFlow()

    fun checkNetworkState() {
        val connectivityManager =
            getSystemService(getApplication(), ConnectivityManager::class.java)
        val networkCallback = object : NetworkCallback() {

            override fun onAvailable(network: Network) {
                noNetworkFlow.value = false
            }

            override fun onLost(network: Network) {
                noNetworkFlow.value = true
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        }
    }

    fun isNoNetwork(): Boolean {
        val cm = getSystemService(getApplication(), ConnectivityManager::class.java)
        return cm?.isActiveNetworkMetered ?: false
    }
}
