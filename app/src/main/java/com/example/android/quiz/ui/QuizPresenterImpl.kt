package com.example.android.quiz.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import com.example.android.quiz.R
import com.example.android.quiz.data.QuizRepository
import com.example.android.quiz.model.*
import com.example.android.quiz.utils.*
import javax.inject.Inject

class QuizPresenterImpl @Inject constructor(
        private val repo: QuizRepository,
        override val timer : Timer,
        private var halfJoker : HalfJoker,
        private var hintJoker : HintJoker
) : QuizContract.QuizPresenter{

    init {
        Log.d("presenter", "initialized")
    }

    var view: QuizContract.QuizView? = null

    private lateinit var category: Category

    private var questions: ArrayList<Question> = ArrayList()
    private var questionNumber: Int = 0
    private val currentQuestion: Question
        get() = questions[questionNumber]

    override fun initializePresenter(@StringRes category: Int?) {
        setCategory(category)
        getQuestions()
        view?.populateTheQuestion(currentQuestion)
    }

    var score: Int = 0

    var checkedButtonId: Int = -1

    var isSubmitted = false

    override fun setCategory(@StringRes category: Int?) {
        this.category = when (category) {
            R.string.literature -> Category.LITERATURE
            R.string.cinema -> Category.CINEMA
            R.string.science -> Category.SCIENCE
            else -> throw IllegalStateException()
        }
    }

    override fun getQuestions() {
        questions = repo.getQuestionsForCategory(category)
    }

    override fun onHalfClicked() {
        //User can use this joker once for each game
        if (halfJoker.isUsed) {
            view?.showToast(R.string.halfwarning)
            return
        }
        halfJoker.isActive = true
        halfJoker.isUsed = true
        view?.hideTwoOptions(halfJoker.getRandomOptionsToErase(currentQuestion))
    }

    override fun onSubmitClicked(checkedButtonId: Int) {
        //warn if nothing is chosen
        if (checkedButtonId == -1) {
            view?.showToast(R.string.chosenothingwarning)
            return
        }

        isSubmitted = true
        view?.setToAnsweredQuestionState()

        this.checkedButtonId = checkedButtonId

        if (answerIsCorrect(checkedButtonId)) {
            score += 20
        } else { //if it was a wrong answer
            view?.showWrongSelection(checkedButtonId)
        }

        view?.showCorrectOption(currentQuestion.correctOption)

        if (questionNumber == 4) { //if it was the last question
            repo.saveResultsToPrefs(category, score)
            view?.showAlertWithMessage(R.string.your_score, score)
        }
        timer.stop()

    }

    fun answerIsCorrect(checkedButtonId: Int) =
            checkedButtonId == currentQuestion.correctOption.buttonId

    override fun onNextClicked() {
        questionNumber++
        isSubmitted = false
        halfJoker.isActive = false
        hintJoker.isActive = false
        timer.restart()
        view?.populateTheQuestion(currentQuestion)
        view?.setToActiveQuestionState()
    }

    override fun onHintClicked() {
        //User can use this options once over a game
        if (hintJoker.isUsed) {
            view?.showToast(R.string.hintwarning)
            return
        }
        view?.showHint()
        hintJoker.isActive = true
        hintJoker.isUsed = true
    }

    override fun onDestroy(isFinishing: Boolean) {
        view = null
        timer.stop()
    }

    override fun subscribeView(view: QuizContract.QuizView?) {
        this.view = view
        timer.resume()
    }

    override fun writeToBundle(outState: Bundle): Bundle {
        outState.putInt(SCORE, score)
        outState.putInt(QUESTION_NO, questionNumber)
        outState.putInt(CURRENT_TIME, timer.secondsLeft.value ?: 60)
        outState.putBoolean(IS_SUBMITTED, isSubmitted)
        outState.putParcelable(HALF_STATE, halfJoker)
        outState.putParcelable(HINT_STATE, hintJoker)
        return outState
    }

    override fun restorePresenterState(savedInstanceState: Bundle) {
        readFromBundle(savedInstanceState)
        view?.populateTheQuestion(currentQuestion)
        if(isSubmitted){
            view?.setToAnsweredQuestionState()
            timer.stop()
            view?.showCorrectOption(currentQuestion.correctOption)
        }
        if(hintJoker.isActive){
            view?.showHint()
        }
        if(halfJoker.isActive){
            view?.hideTwoOptions(halfJoker.optionsToErase)
        }
    }

    private fun readFromBundle(savedInstanceState: Bundle) {
        score = savedInstanceState.getInt(SCORE)
        questionNumber = savedInstanceState.getInt(QUESTION_NO)
        timer.setCurrentTime(savedInstanceState.getInt(CURRENT_TIME))
        halfJoker = savedInstanceState.getParcelable(HALF_STATE)
        hintJoker = savedInstanceState.getParcelable(HINT_STATE)
        isSubmitted = savedInstanceState.getBoolean(IS_SUBMITTED)
    }
}