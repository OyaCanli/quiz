package com.example.android.quiz

import android.content.Context
import com.example.android.quiz.model.Category
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question

class QuizRepository(private val context: Context){

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
        val res = context.resources
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

    companion object {

        @Volatile
        private var sInstance: QuizRepository? = null

        fun getInstance(context: Context): QuizRepository {
            return sInstance ?: synchronized(QuizRepository::class.java) {
                sInstance ?: QuizRepository(context).also { sInstance = it }
            }
        }
    }
}

