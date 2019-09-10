package com.oyacanli.quiz.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, TestDataSourceModule::class, TestTimerModule::class])
interface TestComponent : AppComponent