package com.wordpress.lonelytripblog.funwithflags;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.wordpress.lonelytripblog.funwithflags.di.AppMainComponent;
import com.wordpress.lonelytripblog.funwithflags.di.DaggerAppMainComponent;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.di.ViewModelModule;
import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by Павел on 09.03.2018.
 */

public class FunWithFlagApp extends Application implements HasActivityInjector {

    private AppMainComponent appMainComponent;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        if (appMainComponent == null) {
            appMainComponent = DaggerAppMainComponent.builder().viewModelModule(
                    new ViewModelModule(getApplicationContext())).build();
        }
        appMainComponent.injectApp(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof MainActivity) {
                    AndroidInjection.inject(activity);
                }
                if (activity instanceof FragmentActivity) {
                    ((FragmentActivity) activity).getSupportFragmentManager()
                            .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
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

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
