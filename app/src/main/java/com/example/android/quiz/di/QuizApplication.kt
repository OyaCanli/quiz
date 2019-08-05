package com.example.android.quiz.di

import android.app.Application
import com.example.android.quiz.ui.QuizPresenter
import dagger.Component
import javax.inject.Singleton

class QuizApplication: Application() {

    lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerQuizApplication_ApplicationComponent.builder()
                .androidModule(AndroidModule(this))
                .build()
    }

    @Singleton
    @Component(modules = [AndroidModule::class])
    interface ApplicationComponent {
        fun getPresenter(): QuizPresenter
    }

}