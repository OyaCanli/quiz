package com.example.android.quiz.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.android.quiz.R
import com.example.android.quiz.model.Option
import com.example.android.quiz.model.Question
import com.example.android.quiz.model.QuizState
import com.example.android.quiz.utils.*
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity(), QuizContract.View, OnClickListener {

    private lateinit var presenter : QuizPresenter

    private var correctOption : RadioButton? = null

    private var name: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        presenter = QuizPresenter(applicationContext, this)

        //hide action bar
        supportActionBar?.hide()

        //Retrieve the intent extra
        val bundle = intent.extras
        name = bundle?.getString(NAME)
        presenter.setCategory(bundle?.getInt(CATEGORY))

        //Get questions for the chosen category
        presenter.getQuestions()

        //set click listeners on these buttons
        showHint.setOnClickListener(this)
        half.setOnClickListener(this)
        categories.setOnClickListener(this)
        submit.setOnClickListener(this)
        next.setOnClickListener(this)

        //todo: Set the background theme according to the category chosen

        //Set the first question
        populateTheQuestion(presenter.currentQuestion)

        //Initialize variables
        if (savedInstanceState == null) {
            presenter.setTimer(60000)
        } else {
            presenter.questionNumber = savedInstanceState.getInt(QUESTIONNUMBER)
            populateTheQuestion(presenter.currentQuestion)
            presenter.score = savedInstanceState.getInt(SCORE)
            presenter.halfLifelineCounter = savedInstanceState.getInt(HALF_COUNTER)
            presenter.hintCounter = savedInstanceState.getInt(HINT_COUNTER)
            presenter.currentMillis = savedInstanceState.getLong(CURRENT_MILLIS)
            presenter.isHintVisible = savedInstanceState.getBoolean(IS_HINT_VISIBLE)
            if(presenter.isHintVisible) hint.visibility = View.VISIBLE

            presenter.isHalfLifeLineActif = savedInstanceState.getBoolean(IS_HALF_ACTIF)
            //optionsToErase = savedInstanceState.getParcelableArrayList<Option>(OPTIONS_TO_ERASE)
            /*if (isHalfLifeLineActif) {
                for(option in optionsToErase){
                    findViewById<RadioButton>(option.buttonId).visibility = View.INVISIBLE
                }
            }*/
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.showHint -> presenter.incrementHintCount()
            R.id.half -> presenter.halfTheOptions()
            R.id.categories -> goBackToCategories()
            R.id.submit -> presenter.checkTheAnswer(options.checkedRadioButtonId)
            R.id.next -> setNextQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(CURRENT_MILLIS, presenter.currentMillis)
        outState.putInt(HINT_COUNTER, presenter.hintCounter)
        outState.putInt(HALF_COUNTER, presenter.halfLifelineCounter)
        outState.putInt(QUESTIONNUMBER, presenter.questionNumber)
        outState.putInt(SCORE, presenter.score)
        outState.putBoolean(IS_HALF_ACTIF, presenter.isHalfLifeLineActif)
        //outState.put(OPTIONS_TO_ERASE, optionsToErase)
        outState.putInt(WRONG_OPTION_ID, options.checkedRadioButtonId)
        outState.putBoolean(IS_HINT_VISIBLE, presenter.isHintVisible)
    }

    private fun goBackToCategories() {
        val builder = AlertDialog.Builder(this@QuizActivity, R.style.Theme_AppCompat_DayNight_Dialog)
        builder.setMessage(R.string.exitwarning)
        builder.setPositiveButton(R.string.quit) { _, _ ->
            val categoriesIntent = Intent(this@QuizActivity, WelcomeActivity::class.java)
            startActivity(categoriesIntent)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.create()
        builder.show()
    }

    override fun showHint() {
        findViewById<TextView>(R.id.hint).visibility = View.VISIBLE
    }

    override fun hideTwoOptions(optionsToErase : ArrayList<Option>) {
        //Two of the options will become invisible
        for(option in optionsToErase){
            findViewById<RadioButton>(option.buttonId).visibility = View.INVISIBLE
        }
    }

    override fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showAlertWithMessage(@StringRes messageRes: Int, parameter : Any?) {
        val message = if(parameter == null) getString(messageRes)
                            else getString(messageRes, parameter)

        val builder = AlertDialog.Builder(this@QuizActivity, R.style.Theme_AppCompat_DayNight_Dialog)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.newgame) { _, _ ->
            finish()
            val categoriesIntent = Intent(this@QuizActivity, WelcomeActivity::class.java)
            // Start the new activity
            startActivity(categoriesIntent)
        }
        builder.setNegativeButton(R.string.quit) { _, _ ->
            val goToHome = Intent(Intent.ACTION_MAIN)
            goToHome.addCategory(Intent.CATEGORY_HOME)
            startActivity(goToHome)
        }
        builder.create()
        builder.show()
    }

    override fun showWrongSelection(checkedButtonId: Int) {
        val wrongOption = findViewById<RadioButton>(checkedButtonId)
        wrongOption.background = resources.getDrawable(R.drawable.false_selection_background)
    }

    override fun showCorrectOption(correctOption: Option) {
        //show correct option in green
        val correctButton = findViewById<RadioButton>(correctOption.buttonId)
        correctButton.background = resources.getDrawable(R.drawable.correct_option_background)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.blinking_animation)
        correctButton.startAnimation(animation)
        val handler = Handler(mainLooper)
        handler.postDelayed({ correctButton.clearAnimation() }, 2000)
    }

    private fun setNextQuestion() {
        presenter.questionNumber++

        populateTheQuestion(presenter.currentQuestion)

        presenter.quizState = QuizState.ACTIVE_QUESTION
    }

    override fun setState(quizState: QuizState) {
        when(quizState){
            QuizState.ACTIVE_QUESTION -> setToActiveQuestionState()
            QuizState.ANSWERED_QUESTION -> setToAnsweredQuestionState()
        }
    }

    private fun setToActiveQuestionState() {
        /*Enable all buttons except Next button. All buttons are visible, hint is invisible.
        Everything returns to defaults state*/
        next.isEnabled = false
        showHint.isEnabled = true
        half.isEnabled = true
        submit.isEnabled = true
        for (i in 0..3) {
            options.getChildAt(i).visibility = View.VISIBLE
            options.getChildAt(i).isEnabled = true
            options.getChildAt(i).background = this.resources.getDrawable(R.drawable.selector)
        }
        hint.visibility = View.INVISIBLE
        time.setTextColor(Color.WHITE)
        options.clearCheck()
        presenter.setTimer(60000)
        correctOption?.clearAnimation()
    }

    private fun setToAnsweredQuestionState() {
        //Disable all buttons except Next button
        next.isEnabled = true
        showHint.isEnabled = false
        half.isEnabled = false
        submit.isEnabled = false
        for (i in 0..3) {
            options.getChildAt(i).isEnabled = false
        }
    }

    private fun populateTheQuestion(currentQuestion : Question?) {
        question.text = currentQuestion?.question
        hint.text = currentQuestion?.hint
        optionA.text = currentQuestion?.option1
        optionB.text = currentQuestion?.option2
        optionC.text = currentQuestion?.option3
        optionD.text = currentQuestion?.option4
    }

    override fun updateTime(currentTime: Long) {
        time.text = currentTime.toString()
        if (currentTime <= 5) {
            time.setTextColor(Color.RED)
        }
    }

    public override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    companion object {
        internal var bestResults: SharedPreferences? = null

        fun bestRecords(): String {
            var listOfBestResults: String
            if (bestResults != null) {
                listOfBestResults = "Best record in Literature: " + bestResults!!.getInt(BEST_SCORE_LIT, 0)
                listOfBestResults += "\nBest record in Cinema: " + bestResults!!.getInt(BEST_SCORE_CIN, 0)
                listOfBestResults += "\nBest record in Science: " + bestResults!!.getInt(BEST_SCORE_SCI, 0)
            } else {
                listOfBestResults = "No results"
            }
            return listOfBestResults
        }
    }
}
