package com.oyacanli.quiz.testdoubles

import com.oyacanli.quiz.data.IQuestionDataSource
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import javax.inject.Inject


class QuestionDataSourceTD @Inject constructor() : IQuestionDataSource {

    override fun getQuestionsForCategory(category: Category): ArrayList<Question> {
        val question = getSampleQuestionForCategory(category)
        return arrayListOf(question)
    }
}

fun getSampleQuestionForCategory(category: Category): Question {
    val question = "${category.name} question"
    val hint = "${category.name} hint"
    val option1 = "${category.name} option1"
    val option2 = "${category.name} option2"
    val option3 = "${category.name} option3"
    val option4 = "${category.name} option4"
    val correctOption = Option.C
    return Question(question, hint, option1, option2, option3, option4, correctOption)
}