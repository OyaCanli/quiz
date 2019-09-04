package com.oyacanli.quiz.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.oyacanli.quiz.R
import com.oyacanli.quiz.common.*
import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.model.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class QuizPresenter @Inject constructor(
        private val repo: IQuizRepository,
        val category : Category
) : QuizContract.IQuizPresenter, LifecycleObserver {

    private var view: QuizContract.IQuizView? = null

    private var viewLifecycle: Lifecycle? = null

    private var questions: ArrayList<Question> = repo.getQuestions(category)
    var questionNumber: Int = 0
    private val currentQuestion: Question
        get() = questions[questionNumber]

    override lateinit var timer : QuizTimer

    var halfJoker = Joker()
        private set

    var hintJoker = Joker()
        private set

    var optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)
        private set

    var score: Int = 0
        private set

    private var checkedButtonId: Int = -1

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
        timer = QuizTimer.getInstance(lifecycleOwner)
        this.view?.populateTheQuestion(currentQuestion)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun destroyView() {
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
        view?.showHint()
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
            Timber.d("hint joker is active after rotation")
            view?.showHint() ?: Timber.d("view is null")
        }
        if (halfJoker.isActive) {
            Timber.d("half joker is active after rotation")
            view?.hideTwoOptions(optionsToErase)
        }
        view?.updateScore(score)
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