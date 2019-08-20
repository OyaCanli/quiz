package com.oyacanli.quiz.di

import com.oyacanli.quiz.ui.QuizContract.QuizPresenter
import com.oyacanli.quiz.ui.QuizPresenterImpl
import dagger.Binds
import dagger.Module

@Module
interface PresenterModule {

    @QuizScreenScope
    @Binds
    fun bindQuizPresenter(presenterImpl : QuizPresenterImpl): QuizPresenter
}