package com.example.android.quiz.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.StringRes
import com.example.android.quiz.QuizRepository
import com.example.android.quiz.R
import com.example.android.quiz.model.*
import com.example.android.quiz.utils.BEST_SCORE_CIN
import com.example.android.quiz.utils.BEST_SCORE_LIT
import com.example.android.quiz.utils.BEST_SCORE_SCI
import com.example.android.quiz.utils.TIME_LIMIT
import java.util.*
import kotlin.collections.ArrayList

class QuizPresenter(context: Context) : QuizContract.Presenter, StateChangeListener {

    var view : QuizContract.View? = null

    var optionsToErase: ArrayList<Option> = arrayListOf(Option.A, Option.B, Option.C, Option.D)

    private lateinit var category: Category

    private val repo = QuizRepository(context)

    var quizState : QuizState = ActiveQuestion(this)
        set(value) {
            field = value
            onStateChanged()
        }

    private var questions: ArrayList<Question?> = ArrayList()

    var questionNumber: Int = 0

    var currentQuestion: Question? = null
        get() = questions[questionNumber]
        private set

    var score: Int = 0

    var currentMillis: Long = TIME_LIMIT
    private var timer: CountDownTimer? = null
    private var hintCounter: Int = 0
    private var halfLifelineCounter: Int = 0
    var checkedButtonId : Int = -1

    private var bestResults: SharedPreferences = context.getSharedPreferences("MyPref", 0)
    private var editor: SharedPreferences.Editor = bestResults.edit()

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

    private fun getRandomOptionsToErase() : ArrayList<Option> {
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

    private fun answerIsCorrect(checkedButtonId: Int) =
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

    fun startTimer() {
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
                saveResults()
                view?.showAlertWithMessage(R.string.timeoutwarning, score)
            }
        }.start()
    }

    override fun onStateChanged() {
        when(quizState){
            is ActiveQuestion -> {
                view?.setToActiveQuestionState()
                if((quizState as ActiveQuestion).hintIsVisible){
                    view?.showHint()
                }
                if((quizState as ActiveQuestion).optionsAreHalven){
                    view?.hideTwoOptions(optionsToErase)
                }
            }
            is SubmittedQuestion -> {
                if((quizState as SubmittedQuestion).answerIsWrong){
                    view?.showWrongSelection()
                    Log.d("QuizPresenter", "answer is wrong")
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
        when (category) {
            Category.LITERATURE -> {
                if (score > bestResults.getInt(BEST_SCORE_LIT, 0)) {
                    saveToSharedPrefs(BEST_SCORE_LIT)
                }
            }
            Category.CINEMA -> {
                if (score > bestResults.getInt(BEST_SCORE_CIN, 0)) {
                    saveToSharedPrefs(BEST_SCORE_CIN)
                }
            }
            Category.SCIENCE -> {
                if (score > bestResults.getInt(BEST_SCORE_SCI, 0)) {
                    saveToSharedPrefs(BEST_SCORE_SCI)
                }
            }
        }
    }

    private fun saveToSharedPrefs(key: String) {
        editor.putInt(key, score)
        editor.apply()
    }

    override fun onDestroy(isFinishing : Boolean) {
        destroyTimer()
        if(isFinishing){
            sInstance = null
        }
        view = null
    }

    override fun subscribeView(view: QuizContract.View?) {
        this.view = view
    }

    companion object {

        @Volatile private var sInstance: QuizPresenter? = null

        fun getInstance(context: Context): QuizPresenter {
            return sInstance ?: synchronized(QuizPresenter::class.java) {
                sInstance ?: QuizPresenter(context).also { sInstance = it }
            }
        }
    }


}