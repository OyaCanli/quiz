package com.example.android.quiz.di

import com.example.android.quiz.data.QuizRepository
import com.example.android.quiz.ui.WelcomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun exposeRepo() : QuizRepository
    fun inject(target: WelcomeActivity)
}