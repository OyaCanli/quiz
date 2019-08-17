package com.example.android.quiz.di

import com.example.android.quiz.data.QuizRepository
import com.example.android.quiz.model.HalfJoker
import com.example.android.quiz.model.HintJoker
import com.example.android.quiz.model.Timer
import com.example.android.quiz.ui.QuizContract.QuizPresenter
import com.example.android.quiz.ui.QuizPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @QuizScreenScope
    @Provides
    fun provideQuizPresenter(
            repo : QuizRepository,
            timer : Timer,
            halfJoker : HalfJoker,
            hintJoker : HintJoker
    ): QuizPresenter = QuizPresenterImpl(repo, timer, halfJoker, hintJoker)
}