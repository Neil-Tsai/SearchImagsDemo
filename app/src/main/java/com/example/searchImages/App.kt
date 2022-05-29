package com.example.searchImages

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class App : Application() {
    companion object {
        lateinit var instance: App
        var screenWidth = 0
        var screenHeight = 0
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

    }
}