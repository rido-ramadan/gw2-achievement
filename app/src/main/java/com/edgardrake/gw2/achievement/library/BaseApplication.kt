package com.edgardrake.gw2.achievement.library

import android.app.Application
import com.edgardrake.gw2.achievement.dagger2.components.ApplicationComponent
import com.edgardrake.gw2.achievement.dagger2.components.DaggerApplicationComponent
import com.edgardrake.gw2.achievement.dagger2.modules.ApplicationModule

abstract class BaseApplication : Application() {

    protected lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        appComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        appComponent.inject(this)
    }

    fun getComponent() : ApplicationComponent = appComponent

    companion object {
        lateinit var instance: Application
            private set
    }
}