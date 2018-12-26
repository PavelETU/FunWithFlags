package com.wordpress.lonelytripblog.funwithflags.util

import android.os.CountDownTimer

interface Counter {
    fun startCounter(callback: CallbackForTimer)
}

class CounterImpl : Counter, CountDownTimer(1500, 1500) {

    private var callback: CallbackForTimer? = null

    override fun onFinish() {
        callback?.doOnTimerStop()
        callback = null
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun startCounter(callback: CallbackForTimer) {
        this.callback = callback
        this.start()
    }

}