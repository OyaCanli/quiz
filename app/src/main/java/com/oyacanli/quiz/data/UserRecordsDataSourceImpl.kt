package com.oyacanli.quiz.data

import android.content.Context
import android.content.SharedPreferences
import com.oyacanli.quiz.R
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.utils.BEST_SCORE_CIN
import com.oyacanli.quiz.utils.BEST_SCORE_LIT
import com.oyacanli.quiz.utils.BEST_SCORE_SCI
import com.oyacanli.quiz.utils.NAME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRecordsDataSourceImpl @Inject constructor(private val context : Context) : UserRecordsDataSource{

    private val prefs : SharedPreferences = context.getSharedPreferences("BestResults", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    override fun saveUserName(name : String) {
        editor.putString(NAME, name)
        editor.apply()
    }

    override fun getUserName() : String? = prefs.getString(NAME, "")

    //Best scores
    override fun saveRecords(category: Category, score: Int) {
        when (category) {
            Category.LITERATURE -> putScore(BEST_SCORE_LIT, score)
            Category.CINEMA -> putScore(BEST_SCORE_CIN, score)
            Category.SCIENCE -> putScore(BEST_SCORE_SCI, score)
        }
    }

    private fun getScore(key : String) = prefs.getInt(key, 0)

    private fun putScore(key : String, score : Int) {
        if (score > prefs.getInt(key, 0)) {
            editor.putInt(key, score)
            editor.apply()
        }
    }

    override fun getBestRecords() = context.getString(R.string.best_results, getScore(BEST_SCORE_LIT), getScore(BEST_SCORE_CIN), getScore(BEST_SCORE_SCI))

}