package com.oyacanli.quiz.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.oyacanli.quiz.R
import com.oyacanli.quiz.di.DaggerQuizComponent
import com.oyacanli.quiz.di.QuizApplication
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import com.oyacanli.quiz.utils.CATEGORY
import com.oyacanli.quiz.utils.NAME
import kotlinx.android.synthetic.main.activity_quiz.*
import timber.log.Timber
import javax.inject.Inject

class QuizActivity : AppCompatActivity(), QuizContract.QuizView, OnClickListener {

    @Inject
    lateinit var presenter: QuizContract.QuizPresenter

    private var name: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        initDagger()

        presenter.subscribeView(this)

        //Retrieve the intent extra
        val bundle = intent.extras
        name = bundle?.getString(NAME)
        val category = bundle?.getInt(CATEGORY)

        presenter.initializePresenter(category)

        if(savedInstanceState != null ){
            presenter.restorePresenterState(savedInstanceState)
        }

        //set click listeners on these buttons
        showHint.setOnClickListener(this)
        half.setOnClickListener(this)
        categories.setOnClickListener(this)
        submit.setOnClickListener(this)
        next.setOnClickListener(this)

        //todo: Set the background theme according to the category chosen

        presenter.timer.secondsLeft.observe(this, Observer { time ->
            updateTime(time)
        })
    }

    override fun onSaveInstanceState(emptyBundle : Bundle) {
        val filledBundle = presenter.writeToBundle(emptyBundle)
        super.onSaveInstanceState(filledBundle)
    }

    private fun initDagger() {
        val appComponent = (application as QuizApplication).component
        val quizComponent = DaggerQuizComponent.builder()
                .appComponent(appComponent)
                .build()

        quizComponent.inject(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.showHint -> presenter.onHintClicked()
            R.id.half -> presenter.onHalfClicked()
            R.id.categories -> goBackToCategories()
            R.id.submit -> presenter.onSubmitClicked(options.checkedRadioButtonId)
            R.id.next -> presenter.onNextClicked()
        }
    }

    private fun goBackToCategories() {
        val builder = AlertDialog.Builder(this@QuizActivity, R.style.Theme_AppCompat_DayNight_Dialog)
        builder.setMessage(R.string.exitwarning)
        builder.setPositiveButton(R.string.quit) { _, _ ->
            openCategoriesActivity()
        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.create()
        builder.show()
    }

    override fun showHint() {
        findViewById<TextView>(R.id.hint).visibility = View.VISIBLE
    }

    override fun hideTwoOptions(optionsToErase: ArrayList<Option>) {
        //Two of the options will become invisible
        for (option in optionsToErase) {
            findViewById<RadioButton>(option.buttonId).visibility = View.INVISIBLE
        }
    }

    override fun showToast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showAlertWithMessage(@StringRes messageRes: Int, parameter: Any?) {
        val message = if (parameter == null) getString(messageRes)
        else getString(messageRes, parameter)

        val builder = AlertDialog.Builder(this@QuizActivity, R.style.Theme_AppCompat_DayNight_Dialog)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.newgame) { _, _ ->
            openCategoriesActivity()
        }
        builder.setNegativeButton(R.string.quit) { _, _ ->
            val goToHome = Intent(Intent.ACTION_MAIN)
            goToHome.addCategory(Intent.CATEGORY_HOME)
            startActivity(goToHome)
        }
        builder.create()
        builder.show()
    }

    private fun openCategoriesActivity() {
        finish()
        val categoriesIntent = Intent(this@QuizActivity, WelcomeActivity::class.java)
        // Start the new activity
        startActivity(categoriesIntent)
    }

    override fun showWrongSelection(@IdRes checkedButtonId : Int) {
        val wrongOption = findViewById<RadioButton>(checkedButtonId)
        wrongOption?.background = resources.getDrawable(R.drawable.false_selection_background)
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

    override fun setToActiveQuestionState() {
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
            options.getChildAt(i).clearAnimation()
        }
        hint.visibility = View.INVISIBLE
        time.setTextColor(Color.WHITE)
        options.clearCheck()
    }

    override fun setToAnsweredQuestionState() {
        //Disable all buttons except Next button
        next.isEnabled = true
        showHint.isEnabled = false
        half.isEnabled = false
        submit.isEnabled = false
        for (i in 0..3) {
            options.getChildAt(i).isEnabled = false
        }
    }

    override fun populateTheQuestion(currentQuestion: Question?) {
        question.text = currentQuestion?.question
        hint.text = currentQuestion?.hint
        optionA.text = currentQuestion?.option1
        optionB.text = currentQuestion?.option2
        optionC.text = currentQuestion?.option3
        optionD.text = currentQuestion?.option4
    }

    override fun updateTime(currentTime: Int) {
        time.text = currentTime.toString()
        if (currentTime <= 5) {
            time.setTextColor(Color.RED)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackToCategories()
    }

    public override fun onDestroy() {
        Timber.d("onDestroy called")
        presenter.onDestroy(isFinishing)
        super.onDestroy()
    }
}
