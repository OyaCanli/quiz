package com.oyacanli.quiz.model


import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.oyacanli.quiz.R


enum class Category(@StringRes val categoryName: Int,
                    @ColorRes val categoryColor: Int) {

    LITERATURE(R.string.literature, R.color.literature),

    CINEMA(R.string.cinema, R.color.cinema),

    SCIENCE(R.string.science, R.color.science)

}