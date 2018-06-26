package com.edgardrake.gw2.achievement.library

import android.app.Application
import android.content.Context

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        context = this
    }

    companion object {
        lateinit var context: Application

        fun getAppContext() : Context {
            return context
        }
    }
}