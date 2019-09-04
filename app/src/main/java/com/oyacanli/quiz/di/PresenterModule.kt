package com.oyacanli.quiz.di

import com.oyacanli.quiz.ui.QuizContract.IQuizPresenter
import com.oyacanli.quiz.ui.QuizPresenter
import dagger.Binds
import dagger.Module

@Module
interface PresenterModule {

    @QuizScreenScope
    @Binds
    fun bindQuizPresenter(presenterImpl : QuizPresenter): IQuizPresenter
}