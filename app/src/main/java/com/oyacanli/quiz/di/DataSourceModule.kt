package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindUserRecordsDataSource(dataSourceImpl : UserRecordsDataSource) : IUserRecordsDataSource

    @Singleton
    @Binds
    fun bindQuestionDataSource(dataSourceImpl : QuestionDataSource) : IQuestionDataSource

    @Singleton
    @Binds
    fun bindQuizRepository(repoImpl : QuizRepository) : IQuizRepository
}