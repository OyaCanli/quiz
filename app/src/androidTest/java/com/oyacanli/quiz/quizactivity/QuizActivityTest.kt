package com.oyacanli.quiz.quizactivity

import android.view.View
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oyacanli.quiz.ui.QuizActivity
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizActivityTest {

    @Rule
    @JvmField
    var activityScenarioRule: ActivityScenarioRule<QuizActivity> = ActivityScenarioRule(QuizActivity::class.java)
    //todo: activity should launch with an intent extra


    private var decorView: View? = null

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity {activity: QuizActivity ->
            decorView = activity.window.decorView
        }
    }

    // Check whether the correct theme is set

    // Check whether the time started

    // Check whether there is a question displayed

    // Check whether the question belongs to the category

    // Click on hint button and check whether hint is shown

    // Click on half joker and check if two of the buttons are hidden

    //Click on submit without choosing anything. Verify a toast is shown and no actiob is taken.

    // Click on a wrong choice, click submit and verify score didn't change

    // Click on a wrong choice, click submit and verify wrong option becomes red, correct option become green

    // Click on a wrong choice, click submit and verify options, jokers and submit buttons are disabled

    // Click on the correct choice, click submit and verify score increased

    // Click on the correct choice, click submit and verify correct option becomes green

    // Click on the correct choice, click submit and verify options, jokers and submit buttons are disabled

    //Simulate time running, when it reaches last five seconds, verify it turns red

    //Simulate time running, when it reaches the end, verify a toast is shown (or snack may be)

    //Simulate time running, when it reaches the end, verify options, jokers and submit buttons are disabled





}