package com.example.android.quiz.ui

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.QuizState

interface QuizContract {

    interface View {
        fun showToast(@StringRes message: Int)
        fun setState(quizState : QuizState)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint()
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection(@IdRes checkedButtonId : Int)
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Long)
    }

    interface Presenter {
        fun setCategory(@StringRes category: Int?)
        fun getQuestions()
        fun halfTheOptions()
        fun incrementHintCount()
        fun checkTheAnswer(@IdRes checkedButtonId : Int)
        fun onDestroy()
    }
}