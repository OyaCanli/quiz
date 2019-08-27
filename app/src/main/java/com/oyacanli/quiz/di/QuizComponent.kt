package com.oyacanli.quiz.di

import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.ui.QuizActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

//Custom scope for QuizActivity and its presenter

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class QuizScreenScope

@QuizScreenScope
@Component(modules = [PresenterModule::class],
        dependencies = [AppComponent::class])
interface QuizComponent {

    @Component.Builder
    interface Builder {
        fun build(): QuizComponent

        @BindsInstance
        fun category(category: Category): Builder

        fun appComponent(appComponent: AppComponent) : Builder
    }

    fun inject(quizActivity: QuizActivity)
}
