package com.example.android.quiz.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.support.annotation.StringRes
import com.example.android.quiz.QuizRepository
import com.example.android.quiz.R
import com.example.android.quiz.model.Category
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question
import com.example.android.quiz.model.QuizState
import com.example.android.quiz.utils.BEST_SCORE_CIN
import com.example.android.quiz.utils.BEST_SCORE_LIT
import com.example.android.quiz.utils.BEST_SCORE_SCI
import java.util.*
import kotlin.collections.ArrayList

class QuizPresenter(context: Context, private var view: QuizContract.View?) : QuizContract.Presenter {

    lateinit var category: Category

    private val repo = QuizRepository(context)

    private var questions: ArrayList<Question?> = ArrayList()

    var questionNumber: Int = 0

    var currentQuestion: Question? = null
        get() = questions[questionNumber]
        private set

    var quizState: QuizState = QuizState.ACTIVE_QUESTION
        set(value) {
            field = value
            view?.setState(value)
        }

    var score: Int = 0

    var currentMillis: Long = 0
    var timer: CountDownTimer? = null

    private val optionsToErase = arrayListOf(Option.A, Option.B, Option.C, Option.D)

    var hintCounter: Int = 0

    var halfLifelineCounter: Int = 0

    var isHalfLifeLineActif: Boolean = false

    var isHintVisible: Boolean = false

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
        if (halfLifelineCounter >= 1) {
            view?.showToast(R.string.halfwarning)
            return
        }
        halfLifelineCounter++
        isHalfLifeLineActif = true
        //Index of the correct option
        val indexOfCorrectOption = questions.get(questionNumber)?.correctOption?.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption!!)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)
        view?.hideTwoOptions(optionsToErase)
    }

    override fun checkTheAnswer(checkedButtonId: Int) {
        //warn if nothing is chosen
        if (checkedButtonId == -1) {
            view?.showToast(R.string.chosenothingwarning)
            return
        }

        quizState = QuizState.ANSWERED_QUESTION

        view?.showCorrectOption(currentQuestion!!.correctOption)

        //if the answer is correct
        if (checkedButtonId == currentQuestion?.correctOption?.buttonId) {
            score += 20
        } else { //if it was a wrong answer
            view?.showWrongSelection(checkedButtonId)
        }

        if (questionNumber == 4) { //if it was the last question
            saveResults()
            view?.showAlertWithMessage(R.string.your_score, score)
        }
        //cancel timer
        destroyTimer()

    }

    override fun incrementHintCount() {
        if (hintCounter >= 1) {
            view?.showToast(R.string.hintwarning)
            return
        }
        hintCounter++
        isHintVisible = true
        view?.showHint()
    }

    fun setTimer(millis: Long) {
        //Todo: Create a lifecycle aware timer?
        timer?.run {
            destroyTimer()
        }
        timer = object : CountDownTimer(millis, 1000) {
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

    override fun onDestroy() {
        destroyTimer()
        view = null
    }


}