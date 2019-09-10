package com.oyacanli.quiz.di

import android.app.Application
import timber.log.Timber


open class QuizApplication: Application() {

    open lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        Timber.plant(Timber.DebugTree())

    }

}