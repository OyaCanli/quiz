package com.oyacanli.quiz.testdoubles

import com.oyacanli.quiz.data.IUserRecordsDataSource
import com.oyacanli.quiz.model.Category
import javax.inject.Inject

class UserRecordsDataSourceTD @Inject constructor(): IUserRecordsDataSource{

    override fun saveRecords(category: Category, score: Int) {

    }

    override fun getBestRecords(): String {
        return ""
    }

    override fun getUserName(): String? {
        return null
    }

    override fun saveUserName(name: String) {

    }
}