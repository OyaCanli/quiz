package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.IQuizTimer
import com.oyacanli.quiz.model.IRandomOptionGenerator
import com.oyacanli.quiz.model.QuizTimer
import com.oyacanli.quiz.model.RandomOptionGenerator
import com.oyacanli.quiz.ui.QuizContract.IQuizPresenter
import com.oyacanli.quiz.ui.QuizPresenter
import dagger.Binds
import dagger.Module

@Module
interface PresenterModule {

    @QuizScreenScope
    @Binds
    fun bindQuizPresenter(presenterImpl : QuizPresenter): IQuizPresenter

    @Binds
    fun bindTimer(timerImp: QuizTimer) : IQuizTimer

    @Binds
    fun bindOptionGenerator(optionGeneratorImpl : RandomOptionGenerator) : IRandomOptionGenerator
}