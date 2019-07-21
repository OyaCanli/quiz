package com.example.android.quiz.model


import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.example.android.quiz.R

sealed class Category(@StringRes val categoryName: Int,
                    @ColorRes val categoryColor: Int) {
    abstract fun getQuestions() : ArrayList<Question>

    class Literature : Category(R.string.literature, R.color.literature){
        override fun getQuestions() : ArrayList<Question>{
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    class Cinema : Category(R.string.cinema, R.color.cinema) {
        override fun getQuestions(): ArrayList<Question> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class Science : Category(R.string.science, R.color.science) {
        override fun getQuestions() : ArrayList<Question>{
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}