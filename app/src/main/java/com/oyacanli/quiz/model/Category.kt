package com.oyacanli.quiz.model


import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.oyacanli.quiz.R


enum class Category(@StringRes val categoryName: Int,
                    @StyleRes val theme: Int) {

    LITERATURE(R.string.literature, R.style.Literature),

    CINEMA(R.string.cinema, R.style.Cinema),

    SCIENCE(R.string.science, R.style.Science)

}