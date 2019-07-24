package com.example.android.quiz.model

enum class QuizState {
    /*When player is reading and trying to answer the question
    Timer is active at this state*/
    ACTIVE_QUESTION,

    //When user submits ans verifies a question
    ANSWERED_QUESTION
}