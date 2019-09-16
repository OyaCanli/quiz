package com.oyacanli.quiz.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.oyacanli.quiz.R
import com.oyacanli.quiz.common.CATEGORY
import com.oyacanli.quiz.common.NAME
import com.oyacanli.quiz.di.QuizApplication
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import kotlinx.android.synthetic.main.activity_quiz.*
import timber.log.Timber
import javax.inject.Inject

class QuizActivity : AppCompatActivity(), QuizContract.IQuizView, OnClickListener {

    @Inject
    lateinit var presenter: QuizContract.IQuizPresenter

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //Retrieve the intent extra
        val bundle = intent.extras
        name = bundle?.getString(NAME)
        val category = getCategoryForString(bundle?.getInt(CATEGORY))

        setTheme(category.theme)
        Timber.d("theme: ${category.name}")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        //Initialize quiz appComponent and inject dependencies into this activity
        (application as QuizApplication).initQuizComponent(category).inject(this)

        presenter.attachView(this, lifecycle)

        //set click listeners on these buttons
        showHint.setOnClickListener(this)
        half.setOnClickListener(this)
        categories.setOnClickListener(this)
        submit.setOnClickListener(this)
        next.setOnClickListener(this)

        if(savedInstanceState != null ){
            presenter.restorePresenterState(savedInstanceState)
        }

        presenter.timer.secondsLeft.observe(this, Observer { time : Int ->
            updateTime(time)
            if(time == 0){
                showToast(R.string.timeoutwarning)
                presenter.isSubmitted = true
            }
        })
    }

    private fun getCategoryForString(@StringRes categoryName: Int?) : Category {
        return when (categoryName) {
            R.string.literature -> Category.LITERATURE
            R.string.cinema -> Category.CINEMA
            R.string.science -> Category.SCIENCE
            else -> throw IllegalStateException()
        }
    }

    override fun onSaveInstanceState(emptyBundle : Bundle) {
        val filledBundle = presenter.writeToBundle(emptyBundle)
        super.onSaveInstanceState(filledBundle)
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

    override fun showHint(hint: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(hint)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    presenter.onHintHidden()
                }
        val alert = builder.create()
        alert.show()
    }

    override fun hideTwoOptions(optionsToErase: ArrayList<Option>) {
        Timber.d("hide options is called. list size: ${optionsToErase.size}")
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
        val categoriesIntent = Intent(this@QuizActivity, CategoriesActivity::class.java)
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
            options.getRadioButtonAt(i)?.run{
                visibility = View.VISIBLE
                isEnabled = true
                background = this.resources.getDrawable(R.drawable.selector)
                clearAnimation()
            }
        }
        time.setBackgroundResource(R.drawable.timer_background)
        options.clearCheck()
    }

    override fun setToSubmittedQuestionState() {
        //Disable all buttons except Next button
        next.isEnabled = true
        showHint.isEnabled = false
        half.isEnabled = false
        submit.isEnabled = false
        for (i in 0..3) {
            options.getRadioButtonAt(i)?.isEnabled = false
        }
    }

    override fun updateScore(newScore: Int) {
        score.text = newScore.toString()
    }

    override fun populateTheQuestion(currentQuestion: Question?) {
        currentQuestion?.let{
            question.text = it.question
            optionA.text = it.option1
            optionB.text = it.option2
            optionC.text = it.option3
            optionD.text = it.option4
        }
    }

    override fun updateTime(currentTime: Int) {
        time.text = currentTime.toString()
        if (currentTime <= 5) {
            time.setBackgroundResource(R.drawable.timer_red_background)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBackToCategories()
    }
}
