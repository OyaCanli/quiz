package com.example.android.quiz.di

import android.app.Application
import timber.log.Timber


class QuizApplication: Application() {

    lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        Timber.plant(Timber.DebugTree())

    }

}