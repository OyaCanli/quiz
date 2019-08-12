package com.example.android.quiz.di

import com.example.android.quiz.QuizRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun exposeRepo() : QuizRepository
}