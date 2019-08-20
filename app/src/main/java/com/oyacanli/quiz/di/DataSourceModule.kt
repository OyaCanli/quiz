package com.oyacanli.quiz.di

import com.oyacanli.quiz.data.QuestionDataSource
import com.oyacanli.quiz.data.QuestionDataSourceImpl
import com.oyacanli.quiz.data.UserRecordsDataSource
import com.oyacanli.quiz.data.UserRecordsDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {
    @Binds
    fun bindUserRecordsDataSource(dataSourceImpl : UserRecordsDataSourceImpl) : UserRecordsDataSource

    @Binds
    fun bindQuestionDataSource(dataSourceImpl : QuestionDataSourceImpl) : QuestionDataSource
}