package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.Category

class TestApplication : QuizApplication() {

    lateinit var testComponent : TestAppComponent

    override fun onCreate() {
        super.onCreate()
        testComponent = initAppComponent() as TestAppComponent
    }

    override fun initAppComponent() : AppComponent {
        return DaggerTestAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun initQuizComponent(category: Category): QuizComponent {
        return DaggerTestQuizComponent.builder()
                .appComponent(testComponent)
                .category(category)
                .build()
    }
}