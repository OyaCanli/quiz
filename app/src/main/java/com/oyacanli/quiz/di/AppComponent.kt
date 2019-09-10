package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.model.IQuizTimer
import com.oyacanli.quiz.ui.CategoriesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataSourceModule::class, TimerModule::class])
interface AppComponent {
    fun exposeRepo() : IQuizRepository
    fun exposeTimer() : IQuizTimer
    fun inject(target: CategoriesActivity)
}