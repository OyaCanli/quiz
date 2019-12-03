package com.oyacanli.quiz.testdoubles

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.oyacanli.quiz.model.IQuizTimer
import timber.log.Timber
import javax.inject.Inject

const val tickInterval : Long = 500

class QuizTimerTD @Inject constructor() : IQuizTimer {

    private var handler = Handler()
    private var runnable: Runnable? = null

    private var _secondsLeft: MutableLiveData<Int> = MutableLiveData()
    override var secondsLeft : LiveData<Int> = _secondsLeft

    override fun setCurrentTime(seconds : Int){
        _secondsLeft.value = seconds
    }

    init {
        _secondsLeft.value = 60
        Timber.d("QuizTimer is initialized")
    }

    override fun restart() {
        _secondsLeft.value = 60
        runTimer()
        Timber.d("QuizTimer started")
    }

    override fun stop() {
        runnable?.let { handler.removeCallbacks(it) }
        Timber.d("QuizTimer paused")
    }

    override fun resume(){
        runTimer()
        Timber.d("QuizTimer resumed")
    }

    override fun runTimer() {
        //Start running a count-down timer
        runnable = Runnable {
            _secondsLeft.value = _secondsLeft.value?.minus(1)

            if(_secondsLeft.value!! > 0){
                handler.postDelayed(runnable!!, tickInterval)
            }
        }

        // This is what initially starts the timer
        handler.postDelayed(runnable!!, tickInterval)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onActivityCreated(){
        resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun activityDestroyed(){
        stop()
    }

}