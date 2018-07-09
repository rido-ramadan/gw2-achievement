package com.edgardrake.gw2.achievement.dagger2.modules

import android.content.Context
import com.edgardrake.gw2.achievement.dagger2.annotations.ApplicationContext
import com.edgardrake.gw2.achievement.library.BaseApplication
import dagger.Module
import dagger.Provides

@Module
@Suppress("unused")
class ApplicationModule(private val application: BaseApplication) {

    @Provides @ApplicationContext
    fun provideContext() : Context = application

    @Provides
    fun provideApplication() : Context = application
}