package com.oyacanli.quiz.data

import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Question


interface IQuestionDataSource {
    fun getQuestionsForCategory(category : Category): ArrayList<Question>
}