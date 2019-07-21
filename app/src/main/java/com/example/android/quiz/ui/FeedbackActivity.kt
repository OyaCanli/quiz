package com.example.android.quiz.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.android.quiz.R
import kotlinx.android.synthetic.main.feedback_activity.*

class FeedbackActivity : AppCompatActivity() {

    private lateinit var message: String
    private var other: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_activity)
        message = getString(R.string.feedbackquestion)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu.removeItem(R.id.feedback)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rules -> showRules()
            R.id.categories -> {
                val intent = Intent(this@FeedbackActivity, WelcomeActivity::class.java)
                startActivity(intent)
            }
            R.id.bestRecords -> showBestRecords()
        }
        return super.onOptionsItemSelected(item)
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

    private fun showBestRecords() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(QuizActivity.bestRecords())
                .setTitle(getString(R.string.best_records))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
        val alert = builder.create()
        alert.show()
    }

    fun sendFeedback(view: View) {
        if (geo.isChecked) message += "\n" + getString(R.string.geography)
        if (pol.isChecked) message += "\n" + getString(R.string.politics)
        if (his.isChecked) message += "\n" + getString(R.string.history)
        if (mus.isChecked) message += "\n" + getString(R.string.music)
        if (spo.isChecked) message += "\n" + getString(R.string.sports)
        message += "\n" + getString(R.string.youadded)
        other = other_et.text.toString()
        if (other != " ") message += "\n" + other
        message += "\n" + getString(R.string.thanks)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: ")
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailsubject))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        val addresses = arrayOf(getString(R.string.mailaddress))
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}