package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.IQuestionDataSource
import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.data.IUserRecordsDataSource
import com.oyacanli.quiz.data.QuizRepository
import com.oyacanli.quiz.testdoubles.QuestionDataSourceTD
import com.oyacanli.quiz.testdoubles.UserRecordsDataSourceTD
import dagger.Binds
import dagger.Module

@Module
interface TestDataSourceModule {

    @Binds
    fun bindUserRecordsDataSource(dataSourceImpl : UserRecordsDataSourceTD) : IUserRecordsDataSource

    @Binds
    fun bindQuestionDataSource(dataSourceImpl : QuestionDataSourceTD) : IQuestionDataSource

    @Binds
    fun bindQuizRepository(repoImpl : QuizRepository) : IQuizRepository
}