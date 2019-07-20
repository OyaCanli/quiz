package com.example.android.quiz.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.android.quiz.*
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*

class QuizActivity : AppCompatActivity(), OnClickListener {

    private var questions: ArrayList<Question>? = null
    private var correctAnswers: ArrayList<Option>? = null
    private var questionNumber: Int = 0
    private var score: Int = 0
    private var hintCounter: Int = 0
    private var halfLifelineCounter: Int = 0
    private var currentMillis: Long = 0
    private var timer: CountDownTimer? = null
    private var correctOption : RadioButton? = null
    private var isPaused: Boolean = false
    private var isNextEnabled: Boolean = false
    private var correctOptionIsShown: Boolean = false
    private var wrongOptionIsShown: Boolean = false
    private var isHalfLifeLineActif: Boolean = false
    private var isHintVisible: Boolean = false
    private var name: String? = null
    private var category: String? = null
    private lateinit var bestResults: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val optionsToErase = arrayListOf(Option.A, Option.B, Option.C, Option.D)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        //Change the color of the status bar
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.statusBarColor = resources.getColor(R.color.status_bar_main)
        }
        //hide action bar
        supportActionBar?.hide()
        //Retrieve the intent extra
        val bundle = intent.extras
        name = bundle?.getString(NAME)

        //Initialize sharedPreferences editor
        bestResults = applicationContext.getSharedPreferences("MyPref", 0)
        editor = bestResults.edit()

        //set clicklisteners on these buttons
        showHint.setOnClickListener(this)
        half.setOnClickListener(this)
        categories.setOnClickListener(this)
        submit.setOnClickListener(this)
        next.setOnClickListener(this)

        //get questions from welcome activity
        questions = WelcomeActivity.questions
        correctAnswers = WelcomeActivity.correctAnswers
        category = WelcomeActivity.category

        //todo: Set the background theme according to the category chosen

        //Set the first question
        populateTheQuestion(questions?.get(questionNumber))

        //Initialize variables
        if (savedInstanceState == null) {
            currentMillis = 60000
            setTimer(currentMillis)
        } else {
            questionNumber = savedInstanceState.getInt(QUESTIONNUMBER)
            populateTheQuestion(questions?.get(questionNumber))
            score = savedInstanceState.getInt(SCORE)
            halfLifelineCounter = savedInstanceState.getInt(HALF_COUNTER)
            hintCounter = savedInstanceState.getInt(HINT_COUNTER)
            currentMillis = savedInstanceState.getLong(CURRENT_MILLIS)
            isPaused = savedInstanceState.getBoolean(IS_PAUSED)
            if(isPaused){
                hint.isEnabled = false
                half.isEnabled = false
                for(i in 0 until 4){
                    options.getChildAt(i).isEnabled = false
                }
                time.text = (currentMillis / 1000).toString()
            } else {
                setTimer(currentMillis)
            }
            isNextEnabled = savedInstanceState.getBoolean(IS_NEXT_ENABLED)
            next.isEnabled = isNextEnabled
            isHintVisible = savedInstanceState.getBoolean(IS_HINT_VISIBLE)
            if(isHintVisible) hint.visibility = View.VISIBLE
            correctOptionIsShown = savedInstanceState.getBoolean(CORRECT_OPTION_IS_SHOWN)
             if (correctOptionIsShown) {
                correctOption = findViewById(questions!!.get(questionNumber).correctOption.buttonId)
                 correctOption?.background = resources.getDrawable(R.drawable.correct_option_background)
            }
            wrongOptionIsShown = savedInstanceState.getBoolean(WRONG_OPTION_IS_SHOWN)
            if (wrongOptionIsShown) {
                findViewById<RadioButton>(savedInstanceState.getInt(WRONG_OPTION_ID)).background = resources.getDrawable(R.drawable.false_selection_background)
            }

            isHalfLifeLineActif = savedInstanceState.getBoolean(IS_HALF_ACTIF)
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
            R.id.showHint -> showHint()
            R.id.half -> halfTheOptions()
            R.id.categories -> goBackToCategories()
            R.id.submit -> checkTheAnswer()
            R.id.next -> setNextQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_MILLIS, currentMillis)
        outState.putInt(HINT_COUNTER, hintCounter)
        outState.putInt(HALF_COUNTER, halfLifelineCounter)
        outState.putInt(QUESTIONNUMBER, questionNumber)
        outState.putInt(SCORE, score)
        outState.putBoolean(IS_HALF_ACTIF, isHalfLifeLineActif)
        //outState.put(OPTIONS_TO_ERASE, optionsToErase)
        outState.putBoolean(CORRECT_OPTION_IS_SHOWN, correctOptionIsShown)
        outState.putBoolean(WRONG_OPTION_IS_SHOWN, wrongOptionIsShown)
        outState.putInt(WRONG_OPTION_ID, options.checkedRadioButtonId)
        outState.putBoolean(IS_PAUSED, isPaused)
        outState.putBoolean(IS_NEXT_ENABLED, isNextEnabled)
        outState.putBoolean(IS_HINT_VISIBLE, isHintVisible)
    }

    private fun setTimer(millis: Long) {
        timer?.run{
            destroyTimer()
        }
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time.text = (millisUntilFinished / 1000).toString()
                currentMillis = millisUntilFinished
                if (millisUntilFinished < 6000) {
                    time.setTextColor(Color.RED)
                }
            }
            override fun onFinish() {
                saveResults()
                val message = getString(R.string.timeoutwarning, score)
                createAlertDialog(message)
            }
        }.start()
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

    private fun showHint() {
        //can use hint only once
        if (hintCounter >= 1) {
            Toast.makeText(this, R.string.hintwarning, Toast.LENGTH_SHORT).show()
            return
        }
        hintCounter++
        val hint = findViewById<TextView>(R.id.hint)
        hint.visibility = View.VISIBLE
        //isHintVisible = true;
    }

    private fun halfTheOptions() {
        //can use this lifeline only once
        if (halfLifelineCounter >= 1) {
            Toast.makeText(this, R.string.halfwarning, Toast.LENGTH_SHORT).show()
            return
        }
        halfLifelineCounter++

        //Index of the correct option
        val indexOfCorrectOption = questions?.get(questionNumber)?.correctOption?.ordinal
        //Remove the correct option from the list, because we don't want to erase the correct option
        optionsToErase.removeAt(indexOfCorrectOption!!)
        //Pick a random item from the rest of the list and keep that option as well
        val randomIndex = Random().nextInt(optionsToErase.size)
        optionsToErase.removeAt(randomIndex)
        //The rest of options will become invisible
        for(option in optionsToErase){
            findViewById<RadioButton>(option.buttonId).visibility = View.INVISIBLE
        }
        isHalfLifeLineActif = true
    }

    private fun checkTheAnswer() {
        val checkedButtonId = options!!.checkedRadioButtonId
        //warn if nothing is chosen
        if (checkedButtonId == -1) {
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show()
            return
        }
        //show correct option in green
        correctOption = findViewById(correctAnswers!![questionNumber].buttonId)
        correctOption?.background = resources.getDrawable(R.drawable.correct_option_background)
        correctOptionIsShown = true;
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.blinking_animation)
        correctOption?.startAnimation(animation)
        val handler = Handler(mainLooper)
        handler.postDelayed({ correctOption!!.clearAnimation() }, 2000)
        //disable lifelines, options temporarily until the next question
        showHint.isEnabled = false
        isPaused = true
        half.isEnabled = false
        for (i in 0..3) {
            options.getChildAt(i).isEnabled = false
        }
        //cancel timer
        destroyTimer()
        //if the answer is correct
        if (checkedButtonId == correctAnswers!![questionNumber].buttonId) {
            score += 20
            if (questionNumber == 4) { //if it was the last question
                saveResults()
                val message = getString(R.string.congratulations, name)
                createAlertDialog(message)
            } else { //if it was not the last question
                next.isEnabled = true
                //isNextEnabled = true;
                Toast.makeText(this, R.string.correcttoastmessage, Toast.LENGTH_SHORT).show()
            }
        } else { //if it was a wrong answer
            saveResults()
            val wrongOption = findViewById<RadioButton>(checkedButtonId)
            wrongOption.background = resources.getDrawable(R.drawable.false_selection_background)
            //wrongOptionIsShown = true;
            //Wait two seconds before opening the dialog so that user sees the right answer
            val delayHandler = Handler(mainLooper)
            delayHandler.postDelayed({
                val message = resources.getString(R.string.wronganswer, score)
                createAlertDialog(message)
            }, 2000)
        }
    }

    fun createAlertDialog(message: String) {
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

    private fun setNextQuestion() {
        questionNumber++

        populateTheQuestion(questions?.get(questionNumber))

        for (i in 0..3) {
            options.getChildAt(i).visibility = View.VISIBLE
            options.getChildAt(i).isEnabled = true
            options.getChildAt(i).background = this.resources.getDrawable(R.drawable.selector)
        }
        hint.visibility = View.INVISIBLE
        isHintVisible = false
        time.setTextColor(Color.WHITE)
        options.clearCheck()
        next.isEnabled = false
        isNextEnabled = false
        showHint.isEnabled = true
        isPaused = false
        half.isEnabled = true
        setTimer(60000)
        correctOption?.clearAnimation()
        correctOptionIsShown = false
        wrongOptionIsShown = false
        isHalfLifeLineActif = false
    }

    private fun populateTheQuestion(currentQuestion : Question?) {
        question.text = currentQuestion?.question
        hint.text = currentQuestion?.hint
        optionA.text = currentQuestion?.option1
        optionB.text = currentQuestion?.option2
        optionC.text = currentQuestion?.option3
        optionD.text = currentQuestion?.option4
    }

    fun saveResults() {
        when(category) {
            getString(R.string.literature) -> {
                if (score > bestResults.getInt(BEST_SCORE_LIT, 0)) {
                    saveToSharedPrefs(BEST_SCORE_LIT)
                }
            }
            getString(R.string.cinema) -> {
                if (score > bestResults.getInt(BEST_SCORE_CIN, 0)) {
                    saveToSharedPrefs(BEST_SCORE_CIN)
                }
            }
            getString(R.string.science) -> {
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

    public override fun onDestroy() {
        destroyTimer()
        super.onDestroy()
    }

    private fun destroyTimer() {
        timer?.cancel()
        timer = null
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
