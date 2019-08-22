package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.*
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {
    @Binds
    fun bindUserRecordsDataSource(dataSourceImpl : UserRecordsDataSource) : IUserRecordsDataSource

    @Binds
    fun bindQuestionDataSource(dataSourceImpl : QuestionDataSource) : IQuestionDataSource

    @Binds
    fun bindQuizRepository(repoImpl : QuizRepository) : IQuizRepository
}