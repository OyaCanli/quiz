package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.ITimer
import com.oyacanli.quiz.model.Timer
import com.oyacanli.quiz.ui.QuizContract.IQuizPresenter
import com.oyacanli.quiz.ui.QuizPresenter
import dagger.Binds
import dagger.Module

@Module
interface PresenterModule {

    @QuizScreenScope
    @Binds
    fun bindQuizPresenter(presenterImpl : QuizPresenter): IQuizPresenter

    @QuizScreenScope
    @Binds
    fun bindTimer(timerImpl : Timer): ITimer
}