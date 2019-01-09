package com.example.ilya4.koshpushover

import android.app.Application
import android.content.Context
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        Log.v(TAG, "create app")
    }

    companion object {
        var context: Context? = null
            private set
        private val TAG = ".APPMAIN"
    }
}
