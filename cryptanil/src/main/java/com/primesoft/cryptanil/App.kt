package com.primesoft.cryptanil

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import java.util.*

val app = App.instance

class App : Application() {

    var token = ""

    var language: String? = null

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    fun generateOrderID() = UUID.randomUUID().toString()

}