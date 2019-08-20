package com.oyacanli.quiz.model

import androidx.annotation.IdRes
import com.oyacanli.quiz.R

enum class Option(@IdRes val buttonId: Int){
    A(R.id.optionA),
    B(R.id.optionB),
    C(R.id.optionC),
    D(R.id.optionD)
}