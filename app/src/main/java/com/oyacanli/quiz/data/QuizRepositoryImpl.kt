package com.oyacanli.quiz.data


import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl
@Inject constructor(private val questionDataSource : QuestionDataSource,
                    private val userRecordsDataSource: UserRecordsDataSource)
    :QuizRepository {

    override fun getQuestions(category: Category) : ArrayList<Question> = questionDataSource.getQuestionsForCategory(category)

    override fun saveResults(category: Category, score: Int) = userRecordsDataSource.saveRecords(category, score)

    override fun getBestRecords() : String = userRecordsDataSource.getBestRecords()
}

