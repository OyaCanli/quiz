package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.IQuizTimer
import com.oyacanli.quiz.model.QuizTimer
import dagger.Binds
import dagger.Module

@Module
interface TimerModule {

    @Binds
    fun bindTimer(timerImp: QuizTimer) : IQuizTimer
}