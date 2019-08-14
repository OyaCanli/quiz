package com.example.android.quiz.ui

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question
import com.example.android.quiz.model.StateChangeListener

interface QuizContract {

    interface QuizView {
        fun showToast(@StringRes message: Int)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint()
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection()
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Int)
        fun setToAnsweredQuestionState()
        fun setToActiveQuestionState()
        fun populateTheQuestion(currentQuestion : Question?)
    }

    interface QuizPresenter : StateChangeListener {
        val currentQuestion : Question?
        var currentTime : LiveData<Int>
        val checkedButtonId: Int
        fun subscribeView(view: QuizView?)
        fun setCategory(@StringRes category: Int?)
        fun getQuestions()
        fun halfTheOptions()
        fun onHintClicked()
        fun submit(@IdRes checkedButtonId : Int)
        fun onNextClicked()
        fun onDestroy(isFinishing : Boolean)
    }
}