package com.example.android.quiz

import android.content.SharedPreferences
import android.content.res.Resources
import com.example.android.quiz.model.Category
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question
import com.example.android.quiz.utils.BEST_SCORE_CIN
import com.example.android.quiz.utils.BEST_SCORE_LIT
import com.example.android.quiz.utils.BEST_SCORE_SCI
import javax.inject.Inject

class QuizRepository @Inject constructor(){

    @Inject lateinit var res : Resources
    @Inject lateinit var bestResults: SharedPreferences

    fun getQuestionsForCategory(category : Category): ArrayList<Question?>{
        val correctAnswers = when(category){
            Category.LITERATURE -> literatureCorrectAnswers
            Category.CINEMA -> cinemaCorrectAnswers
            Category.SCIENCE -> scienceCorrectAnswers
        }

        return when(category){
            Category.LITERATURE -> extractTypedArray(R.array.literature_questions, correctAnswers)
            Category.CINEMA -> extractTypedArray(R.array.cinema_questions, correctAnswers)
            Category.SCIENCE -> extractTypedArray(R.array.science_questions, correctAnswers)
        }
    }

    //Extract the question, hints and options from array.xml
    private fun extractTypedArray(arrayId: Int, correctAnswers : ArrayList<Option>): ArrayList<Question?> {
        val list = ArrayList<Question?>()
        val typedArray = res.obtainTypedArray(arrayId)
        val length = typedArray.length()
        for (i in 0 until length) {
            val id = typedArray.getResourceId(i, 0)
            val questionTexts = res.getStringArray(id)
            list.add(Question(questionTexts[0], questionTexts[1], questionTexts[2], questionTexts[3], questionTexts[4], questionTexts[5], correctAnswers[i]))
        }
        typedArray.recycle()
        return list
    }

    private val literatureCorrectAnswers : ArrayList<Option> = arrayListOf(Option.D, Option.B, Option.C, Option.B, Option.A)
    private val cinemaCorrectAnswers : ArrayList<Option> = arrayListOf(Option.A, Option.D, Option.B, Option.A, Option.C)
    private val scienceCorrectAnswers: ArrayList<Option> = arrayListOf(Option.A, Option.A, Option.C, Option.D, Option.B)

    fun saveResultsToPrefs(category: Category, score: Int){
        when (category) {
            Category.LITERATURE -> saveToSharedPrefs(BEST_SCORE_LIT, score)
            Category.CINEMA -> saveToSharedPrefs(BEST_SCORE_CIN, score)
            Category.SCIENCE -> saveToSharedPrefs(BEST_SCORE_SCI, score)
        }
    }

    private fun saveToSharedPrefs(key: String, score: Int) {
        if (score > bestResults.getInt(key, 0)) {
            val editor = bestResults.edit()
            editor.putInt(key, score)
            editor.apply()
        }
    }

    /*companion object {

        @Volatile
        private var sInstance: QuizRepository? = null

        fun getInstance(context: Context): QuizRepository {
            return sInstance ?: synchronized(QuizRepository::class.java) {
                sInstance ?: QuizRepository(context).also { sInstance = it }
            }
        }
    }*/
}

