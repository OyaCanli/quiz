package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.ui.CategoriesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataSourceModule::class])
interface AppComponent {
    fun exposeRepo() : IQuizRepository
    fun inject(target: CategoriesActivity)
}