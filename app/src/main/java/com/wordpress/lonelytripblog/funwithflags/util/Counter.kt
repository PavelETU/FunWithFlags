package com.wordpress.lonelytripblog.funwithflags.util

import android.os.CountDownTimer
import com.wordpress.lonelytripblog.funwithflags.data.CallbackForTimer

open class Counter: CountDownTimer(1500, 1500) {

    private var callback: CallbackForTimer? = null

    override fun onFinish() {
        callback?.doOnTimerStop()
        callback = null
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    open fun startCounter(callback: CallbackForTimer) {
        this.callback = callback
        this.start()
    }

}