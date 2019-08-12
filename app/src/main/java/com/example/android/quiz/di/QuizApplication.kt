package com.example.android.quiz.di

import android.app.Application


class QuizApplication: Application() {

    lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

    }

}