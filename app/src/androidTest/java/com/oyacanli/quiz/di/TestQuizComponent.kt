package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.Category
import dagger.BindsInstance
import dagger.Component

@QuizScreenScope
@Component(modules = [TestPresenterModule::class, TestTimerModule::class],
        dependencies = [TestAppComponent::class])
interface TestQuizComponent : QuizComponent {

    @Component.Builder
    interface Builder {
        fun build(): TestQuizComponent

        @BindsInstance
        fun category(category: Category): Builder

        fun appComponent(appComponent: TestAppComponent) : Builder
    }
}