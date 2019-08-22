package com.oyacanli.quiz.data


import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository
@Inject constructor(private val questionDataSource : IQuestionDataSource,
                    private val userRecordsDataSource: IUserRecordsDataSource)
    :IQuizRepository {

    override fun getQuestions(category: Category) : ArrayList<Question> = questionDataSource.getQuestionsForCategory(category)

    override fun saveResults(category: Category, score: Int) = userRecordsDataSource.saveRecords(category, score)

    override fun getBestRecords() : String = userRecordsDataSource.getBestRecords()
}

