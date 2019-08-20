package com.oyacanli.quiz.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import com.oyacanli.quiz.R
import com.oyacanli.quiz.data.QuizRepositoryImpl
import com.oyacanli.quiz.model.*
import com.oyacanli.quiz.model.Timer
import com.oyacanli.quiz.utils.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class QuizPresenterImpl @Inject constructor(
        private val repo: QuizRepositoryImpl,
        override val timer : Timer
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

    private var halfJoker = Joker()
    private var hintJoker = Joker()
    var optionsToErase: java.util.ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)

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
        questions = repo.getQuestions(category)
    }

    override fun onHalfClicked() {
        //User can use this joker once for each game
        if (halfJoker.isUsed) {
            view?.showToast(R.string.halfwarning)
            return
        }
        halfJoker.isActive = true
        halfJoker.isUsed = true
        view?.hideTwoOptions(getRandomOptionsToErase(currentQuestion))
    }

    private fun getRandomOptionsToErase(question: Question): ArrayList<Option> {
        //Index of the correct option
        val indexOfCorrectOption = question.correctOption.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)

        return optionsToErase
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
            repo.saveResults(category, score)
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
        outState.putEnumList(OPTIONS_TO_ERASE, optionsToErase)
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
            view?.hideTwoOptions(optionsToErase)
        }
    }

    private fun readFromBundle(savedInstanceState: Bundle) {
        with(savedInstanceState){
            score = getInt(SCORE)
            questionNumber = getInt(QUESTION_NO)
            timer.setCurrentTime(getInt(CURRENT_TIME))
            halfJoker = getParcelable(HALF_STATE) ?: Joker()
            hintJoker = getParcelable(HINT_STATE) ?: Joker()
            isSubmitted = getBoolean(IS_SUBMITTED)
            optionsToErase = getEnumList(OPTIONS_TO_ERASE)
        }
    }
}