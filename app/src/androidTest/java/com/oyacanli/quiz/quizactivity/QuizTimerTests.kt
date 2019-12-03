package com.oyacanli.quiz.quizactivity

import android.content.Intent
import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oyacanli.quiz.R
import com.oyacanli.quiz.common.CATEGORY
import com.oyacanli.quiz.common.NAME
import com.oyacanli.quiz.lessThen
import com.oyacanli.quiz.testdoubles.tickInterval
import com.oyacanli.quiz.textWithDrawableId
import com.oyacanli.quiz.ui.QuizActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizTimerTests {

    @Rule
    @JvmField
    var activityScenarioRule: ActivityScenarioRule<QuizActivity> = ActivityScenarioRule(getSampleIntent())

    private fun getSampleIntent() : Intent {
        val context = InstrumentationRegistry.getTargetContext()
        val intentSample = Intent(context, QuizActivity::class.java)
        intentSample.putExtra(NAME, sampleName)
        intentSample.putExtra(CATEGORY, sampleCategory)
        return intentSample
    }

    // Check whether the time started and whether it has default background
    @Test
    fun atLaunch_timeStarted() {
        Thread.sleep(tickInterval)
        onView(withId(R.id.time)).check(matches(lessThen(60)))

        onView(withId(R.id.time)).check(matches(textWithDrawableId(R.drawable.timer_background)))
    }

    //when time reaches last five seconds, verify it turns red
    @Test
    fun lastFiveSeconds_timersTurnsRed() {
        activityScenarioRule.scenario.onActivity {
            it.presenter.timer.setCurrentTime(5)
        }

        onView(withId(R.id.time)).check(matches(textWithDrawableId(R.drawable.timer_red_background)))
    }

    //When time reaches the end, verify a toast is shown (or snack may be)
    @Test
    fun timeOver_showsToast() {
        var decorView : View? = null
        activityScenarioRule.scenario.onActivity {
            it.presenter.timer.setCurrentTime(0)
            decorView = it.window.decorView
        }

        onView(withText(R.string.timeoutwarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
    }

    //When time reaches the end, verify options, jokers and submit buttons are disabled
    @Test
    fun timeOver_jokersAndButtonsDisabled() {
        activityScenarioRule.scenario.onActivity {
            it.presenter.timer.setCurrentTime(0)
        }
        //Verify that jokers, radiobuttons and submit buttons are disabled
        onView(withId(R.id.showHint)).check(matches(not(isEnabled())))
        onView(withId(R.id.half)).check(matches(not(isEnabled())))
        onView(withId(R.id.submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.optionA)).check(matches(not(isEnabled())))
        onView(withId(R.id.optionB)).check(matches(not(isEnabled())))
        onView(withId(R.id.optionC)).check(matches(not(isEnabled())))
        onView(withId(R.id.optionD)).check(matches(not(isEnabled())))

        //Verify next button is enabled
        onView(withId(R.id.next)).check(matches(isEnabled()))
    }
}