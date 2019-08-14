package com.example.android.quiz.model

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import javax.inject.Inject


class Timer @Inject constructor(){

    private var handler = Handler()
    private lateinit var runnable: Runnable

    private var _secondsLeft: MutableLiveData<Int> = MutableLiveData()
    var secondsLeft : LiveData<Int> = _secondsLeft

    fun setCurrentTime(seconds : Int){
        _secondsLeft.value = seconds
    }

    init {
        _secondsLeft.value = 60
        Timber.d("Timer is initialized")
    }

    fun restart() {
        _secondsLeft.value = 60
        runTimer()
        Timber.d("Timer started")

    }

    fun stop() {
        handler.removeCallbacks(runnable)
        Timber.d("Timer paused")
    }

    fun resume(){
        runTimer()
        Timber.d("Timer resumed")
    }

    private fun runTimer() {
        //Start running a count-down timer
        runnable = Runnable {
            _secondsLeft.value = _secondsLeft.value?.minus(1)
            Timber.d("Current time: ${_secondsLeft.value}")
            if(_secondsLeft.value != 0 ){
                handler.postDelayed(runnable, 1000)
            }
        }

        // This is what initially starts the timer
        handler.postDelayed(runnable, 1000)
    }
}