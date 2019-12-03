package com.oyacanli.quiz.categoryactivity


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oyacanli.quiz.R
import com.oyacanli.quiz.ui.CategoriesActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


const val SAMPLE_NAME = "Oya"

@RunWith(AndroidJUnit4::class)
class CategoriesActivityTest{

   @Rule @JvmField
    var activityScenarioRule: ActivityScenarioRule<CategoriesActivity> = ActivityScenarioRule(CategoriesActivity::class.java)

    //Click on start button without choosing anything
    @Test
    fun clickStartWithNoChoice_returnsAWarning() {
        //We need this for testing toast
        var decorView: View? = null
        activityScenarioRule.scenario.onActivity {activity: CategoriesActivity ->
            decorView = activity.window.decorView
        }

        //Click on start button
        onView(withId(R.id.startButton))
                .perform(click())

        //Verify if it shows a toast
        onView(withText(R.string.chosenothingwarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
    }

}