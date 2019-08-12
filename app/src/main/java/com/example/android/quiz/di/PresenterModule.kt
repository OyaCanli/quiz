package com.example.android.quiz.di

import com.example.android.quiz.QuizRepository
import com.example.android.quiz.ui.QuizContract.QuizPresenter
import com.example.android.quiz.ui.QuizPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @QuizScreenScope
    @Provides
    fun provideQuizPresenter(repo : QuizRepository): QuizPresenter = QuizPresenterImpl(repo)
}