package com.oyacanli.quiz.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideResources(): Resources {
        return app.resources
    }
}