package com.oyacanli.quiz.ui

import com.oyacanli.quiz.data.IQuizRepository
import com.oyacanli.quiz.model.Category
import com.oyacanli.quiz.model.ITimer
import com.oyacanli.quiz.model.Option
import com.oyacanli.quiz.model.Question
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class QuizPresenterTest {

    private lateinit var SUT : QuizPresenter
    @Mock lateinit var repo: IQuizRepository
    @Mock lateinit var timer : ITimer

    @Before
    fun setUp() {
        SUT = QuizPresenter(repo, timer)
        `when`(repo.getQuestions(Category.LITERATURE)).thenReturn(getSampleQuestions())
        SUT.initializePresenter(Category.LITERATURE.categoryName)
    }

    @Test
    fun categoryIsSet() {
        assertEquals(SUT.category, Category.LITERATURE)
    }

    /*When half joker is clicked, assuming it was not previously used, it becomes active,
        and set as used*/
    @Test
    fun onHalfClicked_halfNotPreviouslyUsed_halfJokerBecomesActive() {
        SUT.onHalfClicked()
        assert(SUT.halfJoker.isActive)
        assert(SUT.halfJoker.isUsed)
    }

    //When half is previously used, it doesn't become active when clicked
    @Test
    fun onHalfClicked_halfPreviouslyUsed_itDoesNotActivateAgain() {
        SUT.halfJoker.isUsed = true
        SUT.halfJoker.isActive = false
        SUT.onHalfClicked()
        assertFalse(SUT.halfJoker.isActive)
    }

    /*When half the options method is called,
        size of optionsToErase diminishes to 2*/
    @Test
    fun halfTheOptions_optionsToEraseListIsHalven() {
        assertEquals(SUT.optionsToErase.size, 4)
        SUT.halfTheOptions(getSampleQuestion())
        assertEquals(SUT.optionsToErase.size, 2)
    }

    /*When half the options method is called,
    returned optionsToErase doesn't contain the correct option*/
    @Test
    fun halfTheOptions_optionsToEraseDoesNotContainTheCorrectOption() {
        val sampleQuestion = getSampleQuestion()
        SUT.halfTheOptions(sampleQuestion)
        assertFalse(SUT.optionsToErase.contains(sampleQuestion.correctOption))
    }

    /*When hint joker is clicked, assuming it was not previously used, it becomes active,
    and set as used*/
    @Test
    fun onHintClicked_hintPreviouslyNotUsed_hintBecomesActive() {
        SUT.onHintClicked()
        assert(SUT.hintJoker.isActive)
        assert(SUT.hintJoker.isUsed)
    }

    //When hint is previously used, it doesn't become active again when clicked
    @Test
    fun onHintClicked_hintPreviouslyUsed_itDoesNotActivateAgain() {
        SUT.hintJoker.isUsed = true
        SUT.hintJoker.isActive = false
        SUT.onHintClicked()
        assertFalse(SUT.hintJoker.isActive)
    }

    @Test
    fun answerIsCorrect_withCorrectAnswer_returnsTrue() {
        val result = SUT.answerIsCorrect(Option.C.buttonId)
        assert(result)
    }

    @Test
    fun answerIsCorrect_withFalseAnswer_returnsFalse() {
        val result = SUT.answerIsCorrect(Option.D.buttonId)
        assertFalse(result)
    }

    @Test
    fun onSubmitClicked_withNoOptionsChosen_submissionNotRealized() {
        SUT.onSubmitClicked(-1)
        assertFalse(SUT.isSubmitted)
    }

    @Test
    fun onSubmitClicked_withValidOption_submissionRealized() {
        SUT.onSubmitClicked(Option.A.buttonId)
        assert(SUT.isSubmitted)
    }

    @Test
    fun onSubmitClicked_withValidOption_timerStopped() {
        SUT.onSubmitClicked(Option.A.buttonId)
        verify(timer).stop()
    }

    /*@Test
    fun onSubmitClicked_onLastQuestion_resultsAreSaved() {
        SUT.questionNumber = 4
        val acInt : ArgumentCaptor<Int> = ArgumentCaptor.forClass(Int::class.java)
        val acCategory : ArgumentCaptor<Category> = ArgumentCaptor.forClass(Category::class.java)
        SUT.onSubmitClicked(Option.A.buttonId)
        assertNotNull(SUT.category)
        verify(repo).saveResults(acCategory.capture(), acInt.capture())
        assertEquals(acCategory.value, Category.LITERATURE)
        assertEquals(acInt.value, SUT.score)
    }*/

    @Test
    fun onSubmitClicked_withCorrectAnswer_scoreIncrements() {
        SUT.onSubmitClicked(Option.C.buttonId)
        assertEquals(SUT.score, 20 )
    }

    @Test
    fun onSubmitClicked_withFalseAnswer_scoreDoesNotChange() {
        SUT.onSubmitClicked(Option.B.buttonId)
        assertEquals(SUT.score, 0 )
    }

    @Test
    fun onNextClicked_questionNumberIncremented() {
        SUT.onNextClicked()
        assertEquals(SUT.questionNumber, 1)
    }

    @Test
    fun onNextClicked_jokersSetToInactive() {
        SUT.onNextClicked()
        assertFalse(SUT.halfJoker.isActive)
        assertFalse(SUT.hintJoker.isActive)
    }

    @Test
    fun onNextClicked_timerRestarted() {
        SUT.onNextClicked()
        verify(timer).restart()
    }

    @Test
    fun onDestroy_timerStopped() {
        SUT.onDestroy()
        verify(timer).stop()
    }

    ////// HELPER FUNCTIONS //////////////
    private fun getSampleQuestion(): Question {
        val question = "Question itself"
        val hint = "hint"
        val option1 = "option1"
        val option2 = "option2"
        val option3 = "option3"
        val option4 = "option4"
        val correctOption = Option.C
        return Question(question, hint, option1, option2, option3, option4, correctOption)
    }

    private fun getSampleQuestions(): ArrayList<Question> {
        val questions : ArrayList<Question> = ArrayList()
        repeat(5) {
            questions.add(getSampleQuestion())
        }
        return questions
    }
}