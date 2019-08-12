package com.example.android.quiz.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.StringRes
import com.example.android.quiz.QuizRepository
import com.example.android.quiz.R
import com.example.android.quiz.model.*
import com.example.android.quiz.utils.TIME_LIMIT
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class QuizPresenterImpl @Inject constructor(private val repo: QuizRepository) : QuizContract.QuizPresenter, StateChangeListener {

    init {
        Log.d("presenter", "initialized")
    }

    var view: QuizContract.QuizView? = null

    private var optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)

    private lateinit var category: Category

    var quizState: QuizState = ActiveQuestion(this)
        set(value) {
            field = value
            onStateChanged()
        }

    private var questions: ArrayList<Question?> = ArrayList()

    var questionNumber: Int = 0

    override var currentQuestion: Question? = null
        get() = questions[questionNumber]
        private set

    var score: Int = 0

    var currentMillis: Long = TIME_LIMIT
    private var timer: CountDownTimer? = null
    private var hintCounter: Int = 0
    private var halfLifelineCounter: Int = 0
    override var checkedButtonId: Int = -1

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

    override fun halfTheOptions() {
        //User can use this options once over a game
        if (halfLifelineCounter >= 1) {
            view?.showToast(R.string.halfwarning)
            return
        }
        halfLifelineCounter++
        view?.hideTwoOptions(getRandomOptionsToErase())
        (quizState as ActiveQuestion).optionsAreHalven = true
    }

    fun getRandomOptionsToErase(): ArrayList<Option> {
        optionsToErase = arrayListOf(Option.A, Option.B, Option.C, Option.D)
        //Index of the correct option
        val indexOfCorrectOption = questions[questionNumber]?.correctOption?.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption!!)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)

        return optionsToErase
    }

    override fun submit(checkedButtonId: Int) {
        //warn if nothing is chosen
        if (checkedButtonId == -1) {
            view?.showToast(R.string.chosenothingwarning)
            return
        }

        this.checkedButtonId = checkedButtonId

        if (answerIsCorrect(checkedButtonId)) {
            score += 20
            quizState = SubmittedQuestion(false)
        } else { //if it was a wrong answer
            quizState = SubmittedQuestion(true)
        }

        if (questionNumber == 4) { //if it was the last question
            saveResults()
            view?.showAlertWithMessage(R.string.your_score, score)
        }
        //cancel timer
        destroyTimer()
    }

    fun answerIsCorrect(checkedButtonId: Int) =
            checkedButtonId == currentQuestion?.correctOption?.buttonId

    override fun onNextClicked() {
        questionNumber++
        currentMillis = TIME_LIMIT
        quizState = ActiveQuestion(this)
        view?.populateTheQuestion(currentQuestion)
    }

    override fun onHintClicked() {
        //User can use this options once over a game
        if (hintCounter >= 1) {
            view?.showToast(R.string.hintwarning)
            return
        }
        hintCounter++
        (quizState as ActiveQuestion).hintIsVisible = true
    }

    override fun startTimer() {
        //Todo: Create a lifecycle aware timer?
        timer?.run {
            destroyTimer()
        }
        timer = object : CountDownTimer(currentMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view?.updateTime((millisUntilFinished / 1000))
                currentMillis = millisUntilFinished
            }

            override fun onFinish() {
                repo.saveResultsToPrefs(category, score)
                view?.showAlertWithMessage(R.string.timeoutwarning, score)
            }
        }.start()
    }

    override fun onStateChanged() {
        when (quizState) {
            is ActiveQuestion -> {
                view?.setToActiveQuestionState()
                if ((quizState as ActiveQuestion).hintIsVisible) {
                    view?.showHint()
                }
                if ((quizState as ActiveQuestion).optionsAreHalven) {
                    view?.hideTwoOptions(optionsToErase)
                }
            }
            is SubmittedQuestion -> {
                if ((quizState as SubmittedQuestion).answerIsWrong) {
                    view?.showWrongSelection()
                    Log.d("QuizPresenterImpl", "answer is wrong")
                }
                view?.setToAnsweredQuestionState()
                view?.showCorrectOption(currentQuestion!!.correctOption)
            }
        }
    }

    fun destroyTimer() {
        timer?.cancel()
        timer = null
    }

    private fun saveResults() {

    }

    override fun onDestroy(isFinishing: Boolean) {
        destroyTimer()
        /*if (isFinishing) {
            sInstance = null
        }*/
        view = null
    }

    override fun subscribeView(view: QuizContract.QuizView?) {
        this.view = view
    }

    /*companion object {

        @Volatile
        private var sInstance: QuizPresenterImpl? = null

        fun getInstance(): QuizPresenterImpl {
            return sInstance ?: synchronized(QuizPresenterImpl::class.java) {
                sInstance ?: QuizPresenterImpl().also { sInstance = it }
            }
        }
    }*/
}