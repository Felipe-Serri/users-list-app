package com.felipeserri.userlistapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as? ConnectivityManager ?: return false

            val network = connectivityManager.activeNetwork ?: return false

            val capabilities = connectivityManager
                .getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

        } catch (e: Exception) {
            true
        }
    }
}