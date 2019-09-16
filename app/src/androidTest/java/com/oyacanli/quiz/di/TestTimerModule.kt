package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.IQuizTimer
import com.oyacanli.quiz.testdoubles.QuizTimerTD
import dagger.Module
import dagger.Provides

@Module
class TestTimerModule {

    @Provides
    fun provideTimer() : IQuizTimer {
        return QuizTimerTD()
    }
}