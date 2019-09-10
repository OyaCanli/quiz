package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.IQuizTimer
import com.oyacanli.quiz.testdoubles.QuizTimerTD
import dagger.Binds
import dagger.Module

@Module
interface TestTimerModule {

    @Binds
    fun bindTimer(timerImp: QuizTimerTD) : IQuizTimer
}