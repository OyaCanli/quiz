package com.oyacanli.quiz.data

import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Question

interface QuizRepository {

    fun getQuestions(category: Category) : ArrayList<Question>

    fun saveResults(category: Category, score: Int)

    fun getBestRecords() : String
}