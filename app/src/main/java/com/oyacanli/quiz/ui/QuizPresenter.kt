package com.oyacanli.quiz.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.oyacanli.quiz.R
import com.oyacanli.quiz.common.*
import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.di.QuizScreenScope
import com.oyacanli.quiz.model.*
import java.util.*
import javax.inject.Inject

@QuizScreenScope
class QuizPresenter @Inject constructor(
        private val repo: IQuizRepository,
        val category : Category,
        override val timer : IQuizTimer
) : QuizContract.IQuizPresenter, LifecycleObserver {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var view: QuizContract.IQuizView? = null

    var viewLifecycle: Lifecycle? = null

    private var questions: ArrayList<Question> = repo.getQuestions(category)

    var questionNumber: Int = 0

    private val currentQuestion: Question
        get() = questions[questionNumber]

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var halfJoker = Joker()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var hintJoker = Joker()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var score: Int = 0

    private var checkedButtonId: Int = -1

    init {
        timer.resume()
    }

    /*When question is submitted, some buttons should be disabled. This variable keeps
    this data and update ui accordingly*/
   override var isSubmitted : Boolean = false
        set(value) {
            field = value
            if(value) {
                view?.setToSubmittedQuestionState()
            } else {
                view?.setToActiveQuestionState()
            }
        }

    override fun attachView(view: QuizContract.IQuizView, lifecycleOwner: Lifecycle) {
        this.view = view
        viewLifecycle = lifecycleOwner
        viewLifecycle?.addObserver(this)
        this.view?.populateTheQuestion(currentQuestion)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun destroyView() {
        timer.stop()
        view = null
        viewLifecycle = null
    }

    override fun onHalfClicked() {
        //User can use this joker once for each game
        if (halfJoker.isUsed) {
            view?.showToast(R.string.halfwarning)
            return
        }
        halfJoker.isActive = true
        halfJoker.isUsed = true
        halfTheOptions(currentQuestion)
        view?.hideTwoOptions(optionsToErase)
    }

    fun halfTheOptions(question: Question){
        //Index of the correct option
        val indexOfCorrectOption = question.correctOption.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)
    }

    override fun onSubmitClicked(checkedButtonId: Int) {

        //warn if nothing is chosen
        if (checkedButtonId == -1) {
            view?.showToast(R.string.chosenothingwarning)
            return
        }

        isSubmitted = true

        this.checkedButtonId = checkedButtonId

        if (answerIsCorrect(checkedButtonId)) {
            score += 20
            view?.updateScore(score)
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

    fun answerIsCorrect(@IdRes checkedButtonId: Int) =
            checkedButtonId == currentQuestion.correctOption.buttonId

    override fun onNextClicked() {
        questionNumber++
        isSubmitted = false
        halfJoker.isActive = false
        hintJoker.isActive = false
        timer.restart()
        view?.populateTheQuestion(currentQuestion)
    }

    override fun onHintClicked() {
        //User can use this options once over a game
        if (hintJoker.isUsed) {
            view?.showToast(R.string.hintwarning)
            return
        }
        view?.showHint(currentQuestion.hint)
        hintJoker.isActive = true
        hintJoker.isUsed = true
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
        if (isSubmitted) {
            timer.stop()
            view?.showCorrectOption(currentQuestion.correctOption)
        }
        if (hintJoker.isActive) {
            view?.showHint(currentQuestion.hint)
        }
        if (halfJoker.isActive) {
            view?.hideTwoOptions(optionsToErase)
        }
        view?.updateScore(score)
    }

    override fun onHintHidden() {
        hintJoker.isActive = false
    }

    private fun readFromBundle(savedInstanceState: Bundle) {
        with(savedInstanceState) {
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