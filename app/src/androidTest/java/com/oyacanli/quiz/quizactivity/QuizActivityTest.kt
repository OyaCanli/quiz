package com.oyacanli.quiz.quizactivity

import android.content.Intent
import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oyacanli.quiz.R
import com.oyacanli.quiz.buttonWithDrawableId
import com.oyacanli.quiz.common.CATEGORY
import com.oyacanli.quiz.common.NAME
import com.oyacanli.quiz.ui.QuizActivity
import com.oyacanli.quiz.withBackgroundColor
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val sampleName = "Oya"
const val sampleCategory = R.string.literature
const val sampleCorrectOption = R.id.optionC
const val sampleWrongOption = R.id.optionA
const val sampleOptionToErase1 = R.id.optionB
const val sampleOptionToErase2 = R.id.optionD
const val sampleQuestion = "LITERATURE question"
const val sampleHint = "LITERATURE hint"

@RunWith(AndroidJUnit4::class)
class QuizActivityTest {

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

    // Check whether the correct theme is set
    @Test
    fun atLaunch_correctColorsAreSetForCategory() {
        onView(withId(R.id.topBar)).check(matches(withBackgroundColor(R.color.literature)))
        onView(withId(R.id.bottomBar)).check(matches(withBackgroundColor(R.color.literature)))
        onView(withId(R.id.quiz_root)).check(matches(withBackgroundColor(R.color.literature_dark)))
    }

    // Check whether the correct question is displayed
    @Test
    fun atLaunch_correctQuestionDisplayedForCategory() {
        onView(withId(R.id.question)).check(matches(withText(sampleQuestion)))
    }

    // Click on hint button and check whether hint dialog is shown
    @Test
    fun clickHint_launchesHintDialog() {
        onView(withId(R.id.showHint)).perform(click())
        onView(withText(sampleHint)).check(matches(isDisplayed()))
    }

    // Click on half joker and check if two of the buttons are hidden
    @Test
    fun clickHalf_hidesTwoOptions() {
        onView(withId(R.id.half)).perform(click())

        //Verify that previously specified options are hidden
        onView(withId(sampleOptionToErase1)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(sampleOptionToErase2)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))

        //Verify that the correct answer stays visible
        onView(withId(sampleCorrectOption)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    // Click on submit without choosing anything. Verify a toast is shown and no action is taken.
    @Test
    fun clickSubmit_withNoOptionChosen_showsToast() {
        var decorView : View? = null
        activityScenarioRule.scenario.onActivity {activity: QuizActivity ->
            decorView = activity.window.decorView
        }

        //Click on submit without chosing any option
        onView(withId(R.id.submit)).perform(click())

        //Verify if it shows a toast
        onView(withText(R.string.chosenothingwarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
    }


    // Click on a wrong choice, click submit and verify score didn't change
    @Test
    fun clickWrongOptionAndSubmit_scoreNotChanged() {
        //Click on a wrong option.
        onView(withId(sampleWrongOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

        //Verify that the score didn't change
        onView(withId(R.id.score))
                .check(matches(withText("0")))
    }

    // Click on a wrong choice, click submit and verify wrong option becomes red, correct option become green
    @Test
    fun clickWrongOptionAndSubmit_wrongOptionMarked() {
        //Click on a wrong option.
        onView(withId(sampleWrongOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

        //Verify that wrong and correct options are marked with the corresponding drawables
        onView(withId(sampleWrongOption))
                .check(matches(buttonWithDrawableId(R.drawable.false_selection_background)))

        onView(withId(sampleCorrectOption))
                .check(matches(buttonWithDrawableId(R.drawable.correct_option_background)))
    }

    // Click on a wrong choice, click submit and verify options, jokers and submit buttons are disabled
    @Test
    fun clickWrongOptionAndSubmit_submitAndJokersDisabled() {
        //Click on a wrong option.
        onView(withId(sampleWrongOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

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

    // Click on the correct choice, click submit and verify score increased
    @Test
    fun clickCorrectOptionAndSubmit_scoreIncreased() {
        //Click on a wrong option.
        onView(withId(sampleCorrectOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

        //Verify that the score increases
        onView(withId(R.id.score))
                .check(matches(withText("20")))
    }

    // Click on the correct choice, click submit and verify correct option becomes green
    @Test
    fun clickCorrectOptionAndSubmit_correctOptionMarked() {
        //Click on a wrong option.
        onView(withId(sampleCorrectOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

        //Verify that correct option is marked with the corresponding background
        onView(withId(sampleCorrectOption))
                .check(matches(buttonWithDrawableId(R.drawable.correct_option_background)))
    }

    // Click on the correct choice, click submit and verify options, jokers and submit buttons are disabled
    @Test
    fun clickCorrectOptionAndSubmit_submitAndJokersDisabled()  {
        //Click on a wrong option.
        onView(withId(sampleCorrectOption)).perform(click())

        //Click on submit
        onView(withId(R.id.submit)).perform(click())

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