package com.oyacanli.quiz.categoryactivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oyacanli.quiz.R
import com.oyacanli.quiz.common.CATEGORY
import com.oyacanli.quiz.common.NAME
import com.oyacanli.quiz.ui.CategoriesActivity
import com.oyacanli.quiz.ui.QuizActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesActivityIntentTests {

    @Rule
    @JvmField
    var activityRule = IntentsTestRule(CategoriesActivity::class.java)

    //Don't enter a name, but chose a category and click start
    @Test
    fun choseCategoryButNoName_launchesQuizActivityWithExtras() {
        onView(ViewMatchers.withId(R.id.name_entry))
                .perform(clearText())

        //Click on a category
        onView(ViewMatchers.withId(R.id.literatureButton))
                .perform(ViewActions.click())

        //Click on start button
        onView(ViewMatchers.withId(R.id.startButton))
                .perform(ViewActions.click())

        //Verify if it launches an intent with correct extra
        Intents.intended(hasComponent(hasClassName(QuizActivity::class.java.name)))
        Intents.intended(hasExtra(NAME, ""))
        Intents.intended(hasExtra(CATEGORY, R.string.literature))
    }

    //Enter a name, choose a category and click start
    @Test
    fun choseCategoryAndEnterName_launchesQuizActivityWithExtras() {
        //Enter a name
        onView(ViewMatchers.withId(R.id.name_entry))
                .perform(clearText(), ViewActions.typeText(SAMPLE_NAME))

        //Click on a category
        onView(ViewMatchers.withId(R.id.cinemaButton))
                .perform(ViewActions.click())

        //Click on start button
        onView(ViewMatchers.withId(R.id.startButton))
                .perform(ViewActions.click())

        //Verify if it launches an intent with correct extra
        Intents.intended(hasComponent(hasClassName(QuizActivity::class.java.name)))
        Intents.intended(hasExtra(NAME, SAMPLE_NAME))
        Intents.intended(hasExtra(CATEGORY, R.string.cinema))
    }
}