package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.IRandomOptionGenerator
import com.oyacanli.quiz.testdoubles.RandomOptionGeneratorTD
import com.oyacanli.quiz.ui.QuizContract
import com.oyacanli.quiz.ui.QuizPresenter
import dagger.Binds
import dagger.Module

@Module
interface TestPresenterModule {

    @QuizScreenScope
    @Binds
    fun bindQuizPresenter(presenterImpl : QuizPresenter): QuizContract.IQuizPresenter

    @Binds
    fun bindOptionGenerator(optionGeneratorImpl : RandomOptionGeneratorTD) : IRandomOptionGenerator
}