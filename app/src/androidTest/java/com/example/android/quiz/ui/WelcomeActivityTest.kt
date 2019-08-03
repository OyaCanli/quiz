package com.example.android.quiz.ui


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.quiz.R
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


const val SAMPLE_NAME = "Oya"

@RunWith(AndroidJUnit4::class)
class WelcomeActivityTest{

   @Rule @JvmField
    var activityScenarioRule: ActivityScenarioRule<WelcomeActivity> = ActivityScenarioRule(WelcomeActivity::class.java)

    private var decorView: View? = null

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity {activity: WelcomeActivity ->
            decorView = activity.window.decorView
        }
    }

    //Click on start without clicking anything
    @Test
    fun clickStartWithNoChoice_returnsAWarning() {
        //Click on start button
        onView(withId(R.id.startButton))
                .perform(click())

        //Verify if it shows a toast
        onView(withText(R.string.chosenothingwarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
    }

}