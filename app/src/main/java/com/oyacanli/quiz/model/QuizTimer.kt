package com.oyacanli.quiz.model

import android.os.Handler
import androidx.lifecycle.*
import timber.log.Timber


class QuizTimer private constructor(viewLifecycle: Lifecycle) : LifecycleObserver {

    private var handler = Handler()
    private var runnable: Runnable? = null

    private var _secondsLeft: MutableLiveData<Int> = MutableLiveData()
    var secondsLeft : LiveData<Int> = _secondsLeft

    fun setCurrentTime(seconds : Int){
        _secondsLeft.value = seconds
    }

    init {
        viewLifecycle.addObserver(this)
        _secondsLeft.value = 60
        Timber.d("QuizTimer is initialized")
    }

    fun restart() {
        _secondsLeft.value = 60
        runTimer()
        Timber.d("QuizTimer started")
    }

    fun stop() {
        runnable?.let { handler.removeCallbacks(it) }
        Timber.d("QuizTimer paused")
    }

    fun resume(){
        runTimer()
        Timber.d("QuizTimer resumed")
    }

    fun runTimer() {
        //Start running a count-down timer
        runnable = Runnable {
            _secondsLeft.value = _secondsLeft.value?.minus(1)
            //Timber.d("Current time: ${_secondsLeft.value}")
            if(_secondsLeft.value != 0 ){
                handler.postDelayed(runnable!!, 1000)
            }
        }

        // This is what initially starts the timer
        handler.postDelayed(runnable!!, 1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onActivityCreated(){
        resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun activityDestroyed(){
        stop()
    }

    companion object {
        @Volatile private var sInstance: QuizTimer? = null

        fun getInstance(viewLifecycle: Lifecycle): QuizTimer {
            return sInstance ?: synchronized(QuizTimer::class.java) {
                sInstance ?: QuizTimer(viewLifecycle).also { sInstance = it }
            }
        }
    }
}