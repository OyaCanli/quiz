package com.oyacanli.quiz.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import com.oyacanli.quiz.model.QuizTimer

interface QuizContract {

    interface IQuizView {
        fun showToast(@StringRes message: Int)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint(hint: String)
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection(@IdRes checkedButtonId : Int)
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Int)
        fun updateScore(newScore: Int)
        fun setToSubmittedQuestionState()
        fun setToActiveQuestionState()
        fun populateTheQuestion(currentQuestion : Question?)
    }

    interface IQuizPresenter {
        val timer: QuizTimer
        var isSubmitted : Boolean
        fun attachView(view: IQuizView, lifecycleOwner: Lifecycle)
        fun destroyView()
        fun onHalfClicked()
        fun onHintClicked()
        fun onHintHidden()
        fun onSubmitClicked(@IdRes checkedButtonId : Int)
        fun onNextClicked()
        fun writeToBundle(outState: Bundle): Bundle
        fun restorePresenterState(savedInstanceState : Bundle)
    }
}