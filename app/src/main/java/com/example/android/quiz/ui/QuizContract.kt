package com.example.android.quiz.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question
import com.example.android.quiz.model.Timer

interface QuizContract {

    interface QuizView {
        fun showToast(@StringRes message: Int)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint()
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection(@IdRes checkedButtonId : Int)
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Int)
        fun setToAnsweredQuestionState()
        fun setToActiveQuestionState()
        fun populateTheQuestion(currentQuestion : Question?)
    }

    interface QuizPresenter {
        val timer : Timer
        fun initializePresenter(@StringRes category: Int?)
        fun subscribeView(view: QuizView?)
        fun setCategory(@StringRes category: Int?)
        fun getQuestions()
        fun onHalfClicked()
        fun onHintClicked()
        fun onSubmitClicked(@IdRes checkedButtonId : Int)
        fun onNextClicked()
        fun onDestroy(isFinishing : Boolean)
        fun writeToBundle(outState: Bundle): Bundle
        fun restorePresenterState(savedInstanceState : Bundle)
    }
}