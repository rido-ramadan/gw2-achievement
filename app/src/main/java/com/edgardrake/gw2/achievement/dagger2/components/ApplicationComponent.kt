package com.edgardrake.gw2.achievement.dagger2.components

import android.content.Context
import com.edgardrake.gw2.achievement.dagger2.annotations.ApplicationContext
import com.edgardrake.gw2.achievement.dagger2.modules.ApplicationModule
import com.edgardrake.gw2.achievement.library.BaseApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: BaseApplication)

    @ApplicationContext
    fun getContext() : Context
}