package com.example.android.quiz.model

import androidx.annotation.IdRes
import com.example.android.quiz.R

enum class Option(@IdRes val buttonId: Int){
    A(R.id.optionA),
    B(R.id.optionB),
    C(R.id.optionC),
    D(R.id.optionD)
}