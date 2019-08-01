package com.example.android.quiz.model

sealed class QuizState

    /*When player is reading and trying to answer the question
    Timer is active at this state*/
    class ActiveQuestion(private val callback : StateChangeListener) : QuizState(){
        var hintIsVisible : Boolean = false
        set(value) {
            field = value
            callback.onStateChanged()
        }
        var optionsAreHalven : Boolean = false
            set(value) {
                field = value
                callback.onStateChanged()
            }
    }
    //When user submits ans verifies a question
    class SubmittedQuestion(val answerIsWrong : Boolean): QuizState()
