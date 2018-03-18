package com.wordpress.lonelytripblog.funwithflags;

import android.app.Application;

import com.wordpress.lonelytripblog.funwithflags.di.DaggerViewModelComponent;
import com.wordpress.lonelytripblog.funwithflags.di.ViewModelComponent;

/**
 * Created by Павел on 09.03.2018.
 */

public class FunWithFlagApp extends Application {

    private static ViewModelComponent viewModelComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        viewModelComponent = DaggerViewModelComponent.builder().build();
    }

    public static ViewModelComponent getViewModelComponent() {
        return viewModelComponent;
    }

}
