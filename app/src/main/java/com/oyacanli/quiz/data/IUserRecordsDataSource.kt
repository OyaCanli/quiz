package com.oyacanli.quiz.data

import com.oyacanli.quiz.model.Category

interface IUserRecordsDataSource {

    fun saveRecords(category: Category, score: Int)

    fun getBestRecords() : String

    fun getUserName(): String?

    fun saveUserName(name : String)
}