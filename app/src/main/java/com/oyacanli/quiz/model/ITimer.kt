package com.oyacanli.quiz.model

import androidx.lifecycle.LiveData

interface ITimer {
    var secondsLeft : LiveData<Int>
    fun setCurrentTime(seconds : Int)
    fun restart()
    fun stop()
    fun resume()
    fun runTimer()
}