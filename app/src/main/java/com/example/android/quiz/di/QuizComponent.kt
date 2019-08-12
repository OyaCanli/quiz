package com.example.android.quiz.di

import com.example.android.quiz.ui.QuizActivity
import dagger.Component
import javax.inject.Scope

//Custom scope for QuizActivity and its presenter

@Scope
@Retention
annotation class QuizScreenScope

@QuizScreenScope
@Component(modules = [PresenterModule::class],
        dependencies = [AppComponent::class])
interface QuizComponent {
    fun inject(quizActivity : QuizActivity)
}
