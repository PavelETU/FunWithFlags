package com.wordpress.lonelytripblog.funwithflags;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.wordpress.lonelytripblog.funwithflags.di.AppMainComponent;
import com.wordpress.lonelytripblog.funwithflags.di.DaggerAppMainComponent;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.di.ViewModelModule;
import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by Павел on 09.03.2018.
 */

public class FunWithFlagApp extends Application {

    private AppMainComponent appMainComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof MainActivity) {
                    if (appMainComponent == null) {
                        appMainComponent = DaggerAppMainComponent.builder().viewModelModule(
                                new ViewModelModule(getApplicationContext())).build();
                    }
                    appMainComponent.injectToActivityWithGame((MainActivity) activity);
                }
                if (activity instanceof FragmentActivity) {
                    ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
                            if (f instanceof InjectableFragment) {
                                AndroidSupportInjection.inject(f);
                            }
                        }
                    }, true);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

}
