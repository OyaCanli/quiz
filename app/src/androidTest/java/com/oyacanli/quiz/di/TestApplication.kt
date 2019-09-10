package com.oyacanli.quiz.di

class TestApplication : QuizApplication() {

    override lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerTestComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}