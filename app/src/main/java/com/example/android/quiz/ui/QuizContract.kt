package com.example.android.quiz.ui

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question

interface QuizContract {

    interface View {
        fun showToast(@StringRes message: Int)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint()
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection()
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Long)
        fun setToAnsweredQuestionState()
        fun setToActiveQuestionState()
        fun populateTheQuestion(currentQuestion : Question?)
    }

    interface Presenter {
        fun subscribeView(view: View?)
        fun setCategory(@StringRes category: Int?)
        fun getQuestions()
        fun halfTheOptions()
        fun onHintClicked()
        fun submit(@IdRes checkedButtonId : Int)
        fun onNextClicked()
        fun onDestroy(isFinishing : Boolean)
    }
}