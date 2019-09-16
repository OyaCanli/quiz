package com.oyacanli.quiz.di

import android.app.Application
import com.oyacanli.quiz.model.Category
import timber.log.Timber


open class QuizApplication: Application() {

    open lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appComponent = initAppComponent()
    }

    open fun initAppComponent() : AppComponent{
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    open fun initQuizComponent(category: Category) : QuizComponent{
        return DaggerQuizComponent.builder()
                .appComponent(appComponent)
                .category(category)
                .build()
    }

}