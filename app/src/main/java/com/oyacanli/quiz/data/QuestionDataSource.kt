package com.oyacanli.quiz.data

import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Question


interface QuestionDataSource {
    fun getQuestionsForCategory(category : Category): ArrayList<Question>
}