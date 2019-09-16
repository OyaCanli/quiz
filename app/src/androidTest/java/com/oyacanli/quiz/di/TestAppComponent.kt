package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.IQuizRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, TestDataSourceModule::class])
interface TestAppComponent : AppComponent {
    override fun exposeRepo() : IQuizRepository
}