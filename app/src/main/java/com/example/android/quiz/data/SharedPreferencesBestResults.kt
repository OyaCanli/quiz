package com.example.android.quiz.data

import android.content.Context
import android.content.SharedPreferences
import com.example.android.quiz.R
import com.example.android.quiz.model.Category
import com.example.android.quiz.utils.BEST_SCORE_CIN
import com.example.android.quiz.utils.BEST_SCORE_LIT
import com.example.android.quiz.utils.BEST_SCORE_SCI
import com.example.android.quiz.utils.NAME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesBestResults @Inject constructor(private val context : Context){

    private val prefs : SharedPreferences = context.getSharedPreferences("BestResults", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    //User name
    fun getUserName() = prefs.getString(NAME, "")

    fun putUserName(name : String) {
        editor.putString(NAME, name)
        editor.apply()
    }

    //Best scores
    private fun getScore(key : String) = prefs.getInt(key, 0)

    private fun putScore(key : String, score : Int) {
        if (score > prefs.getInt(key, 0)) {
            editor.putInt(key, score)
            editor.apply()
        }
    }

    fun saveResults(category: Category, score: Int){
        when (category) {
            Category.LITERATURE -> putScore(BEST_SCORE_LIT, score)
            Category.CINEMA -> putScore(BEST_SCORE_CIN, score)
            Category.SCIENCE -> putScore(BEST_SCORE_SCI, score)
        }
    }

    fun getBestRecords() = context.getString(R.string.best_results, getScore(BEST_SCORE_LIT), getScore(BEST_SCORE_CIN), getScore(BEST_SCORE_SCI))

}