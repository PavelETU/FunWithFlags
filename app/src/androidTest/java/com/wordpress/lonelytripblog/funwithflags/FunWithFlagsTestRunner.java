package com.wordpress.lonelytripblog.funwithflags;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

public class FunWithFlagsTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestApp.class.getName(), context);
    }
}
