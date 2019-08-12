package com.example.android.quiz.di

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return app.getSharedPreferences("MyPref", 0)
    }

    @Provides
    @Singleton
    fun provideResources(): Resources {
        return app.resources
    }
}