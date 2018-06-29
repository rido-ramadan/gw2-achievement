package com.edgardrake.gw2.achievement.library

import android.app.Application

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}