package com.wordpress.lonelytripblog.funwithflags

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class FunWithFlagsTestRunner : AndroidJUnitRunner() {
    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}