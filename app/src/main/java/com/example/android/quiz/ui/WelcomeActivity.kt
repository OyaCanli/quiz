package com.example.android.quiz.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.android.quiz.*
import kotlinx.android.synthetic.main.welcome_activity.*
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private var menu: Menu? = null
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    internal var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

        startButton.setOnClickListener { startQuiz() }
        name_entry.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(userInput: Editable?) {
                name = userInput.toString()
                editor = prefs.edit()
                editor.putString(NAME, name)
                editor.apply()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        prefs = applicationContext.getSharedPreferences("MyPref", 0)
        name = prefs.getString(NAME, "")
        if (name?.isNotBlank()== true){
            name_entry.setText(name)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        this.menu = menu
        menu.removeItem(R.id.categories)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rules -> showRules()
            R.id.feedback -> {
                val intent = Intent(this@WelcomeActivity, FeedbackActivity::class.java)
                startActivity(intent)
            }
            R.id.bestRecords -> showBestRecords()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBestRecords() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(QuizActivity.bestRecords())
                .setTitle(getString(R.string.best_records))
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id ->
                    //do things
                }
        val alert = builder.create()
        alert.show()
    }

    private fun showRules() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.info))
                .setTitle(getString(R.string.rules))
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id ->
                    //do things
                }
        val alert = builder.create()
        alert.show()
    }

    private fun startQuiz() {
        val checkedButtonId = categoriesGroup.checkedRadioButtonId
        //warns if nothing is checked
        if(checkedButtonId == -1){
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show()
            return
        }
        //assign the questions according to the category chosen
        when(checkedButtonId){
            R.id.literatureButton -> {
                correctAnswers.addAll(literatureCorrectAnswers)
                extractTypedArray(R.array.literature_questions)
                category = getString(R.string.literature)
            }
            R.id.cinemaButton -> {
                correctAnswers.addAll(cinemaCorrectAnswers)
                extractTypedArray(R.array.cinema_questions)
                category = getString(R.string.cinema)
            }
            R.id.scienceButton -> {
                correctAnswers.addAll(scienceCorrectAnswers)
                extractTypedArray(R.array.science_questions)
                category = getString(R.string.science)
            }
        }

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(NAME, name)
        startActivity(intent)
    }

    //Extract the question, hints and options from array.xml
    private fun extractTypedArray(arrayId: Int) {
        val typedArray = resources.obtainTypedArray(arrayId)
        val length = typedArray.length()
        for (i in 0 until length) {
            val id = typedArray.getResourceId(i, 0)
            val questionTexts = resources.getStringArray(id)
            questions.add(Question(questionTexts[0], questionTexts[1], questionTexts[2], questionTexts[3], questionTexts[4], questionTexts[5], correctAnswers[i]))
        }
        typedArray.recycle()
    }

    companion object {
        var correctAnswers = ArrayList<Option>()
        var questions = ArrayList<Question>()
            internal set
        var category: String? = null
            private set
        val NAME = "name"
    }
}
