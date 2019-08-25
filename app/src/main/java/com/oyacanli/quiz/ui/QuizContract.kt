package com.oyacanli.quiz.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.ITimer
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question

interface QuizContract {

    interface IQuizView {
        fun showToast(@StringRes message: Int)
        fun hideTwoOptions(optionsToErase: ArrayList<Option>)
        fun showHint()
        fun showCorrectOption(correctOption: Option)
        fun showWrongSelection(@IdRes checkedButtonId : Int)
        fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any? = null)
        fun updateTime(currentTime : Int)
        fun setToSubmittedQuestionState()
        fun setToActiveQuestionState()
        fun populateTheQuestion(currentQuestion : Question?)
    }

    interface IQuizPresenter {
        val timer : ITimer
        val isSubmitted : LiveData<Boolean>

        fun initializePresenter(category: Category)
        fun subscribeView(view: IQuizView?)
        fun getQuestions()
        fun onHalfClicked()
        fun onHintClicked()
        fun onSubmitClicked(@IdRes checkedButtonId : Int)
        fun onNextClicked()
        fun onDestroy()
        fun writeToBundle(outState: Bundle): Bundle
        fun restorePresenterState(savedInstanceState : Bundle)
        fun setIsSubmitted(submitted: Boolean)
    }
}