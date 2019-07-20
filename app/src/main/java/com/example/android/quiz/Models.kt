package com.example.android.quiz

import android.support.annotation.IdRes

data class Question(val question: String,
                    val hint: String,
                    val option1: String,
                    val option2: String,
                    val option3: String,
                    val option4: String,
                    val correctOption : Option)

enum class Option(@IdRes val buttonId: Int){
    A(R.id.optionA),
    B(R.id.optionB),
    C(R.id.optionC),
    D(R.id.optionD)
}

