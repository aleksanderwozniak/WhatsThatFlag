package com.olq.whatsthatflag.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by olq on 24.11.17.
 */
fun checkInternetConnection(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo

    return activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting
}