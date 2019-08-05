package com.example.android.quiz.di


import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//Taken from Google Samples

@Module
class AndroidModule(private val application: QuizApplication) {

    @Provides @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences("MyPref", 0)
    }

    @Provides @Singleton
    fun provideResources(): Resources {
        return application.resources
    }
}