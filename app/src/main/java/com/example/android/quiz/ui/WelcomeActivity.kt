package com.example.android.quiz.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.android.quiz.R
import com.example.android.quiz.utils.CATEGORY
import com.example.android.quiz.utils.NAME
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var name: String? = null
    @StringRes private var category : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

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
        menu.removeItem(R.id.categories)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rules -> showRules()
            R.id.bestRecords -> showBestRecords()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBestRecords() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(QuizActivity.bestRecords())
                .setTitle(getString(R.string.best_records))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
        val alert = builder.create()
        alert.show()
    }

    private fun showRules() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.info))
                .setTitle(getString(R.string.rules))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
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
        category = when(checkedButtonId){
            R.id.literatureButton -> R.string.literature
            R.id.cinemaButton -> R.string.cinema
            R.id.scienceButton -> R.string.science
            else -> 0
        }

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(NAME, name)
        intent.putExtra(CATEGORY, category)
        startActivity(intent)
    }
}
