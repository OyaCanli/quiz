package com.example.android.quiz.di

import com.example.android.quiz.model.HalfJoker
import com.example.android.quiz.model.HintJoker
import dagger.Module
import dagger.Provides

@Module
class JokerModule {

    @Provides
    fun provideHint() : HintJoker = HintJoker(false, false)

    @Provides
    fun provideHalf() : HalfJoker = HalfJoker(false, false)
}