package com.oyacanli.quiz.data

import android.content.res.Resources
import com.oyacanli.quiz.R
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(val res: Resources) : QuestionDataSource {

    private val literatureCorrectAnswers: ArrayList<Option> = arrayListOf(Option.D, Option.B, Option.C, Option.B, Option.A)
    private val cinemaCorrectAnswers: ArrayList<Option> = arrayListOf(Option.A, Option.D, Option.B, Option.A, Option.C)
    private val scienceCorrectAnswers: ArrayList<Option> = arrayListOf(Option.A, Option.A, Option.C, Option.D, Option.B)

    override fun getQuestionsForCategory(category: Category): ArrayList<Question> {
        val correctAnswers = when (category) {
            Category.LITERATURE -> literatureCorrectAnswers
            Category.CINEMA -> cinemaCorrectAnswers
            Category.SCIENCE -> scienceCorrectAnswers
        }

        return when (category) {
            Category.LITERATURE -> extractTypedArray(R.array.literature_questions, correctAnswers)
            Category.CINEMA -> extractTypedArray(R.array.cinema_questions, correctAnswers)
            Category.SCIENCE -> extractTypedArray(R.array.science_questions, correctAnswers)
        }
    }

    //Extract the question, hints and options from array.xml
    private fun extractTypedArray(arrayId: Int, correctAnswers: ArrayList<Option>): ArrayList<Question> {
        val list = ArrayList<Question>()
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
}