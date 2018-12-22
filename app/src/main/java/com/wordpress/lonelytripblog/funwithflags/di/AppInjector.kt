package com.wordpress.lonelytripblog.funwithflags.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.wordpress.lonelytripblog.funwithflags.FunWithFlagsApp
import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection

object AppInjector {
    fun inject(app: FunWithFlagsApp) {
        DaggerAppMainComponent.builder().funWithFlagsModule(FunWithFlagsModule(app)).build()
                .injectApp(app)
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity is MainActivity) {
                    AndroidInjection.inject(activity)
                }
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            if (f is InjectMe) {
                                AndroidSupportInjection.inject(f)
                            }
                        }

                    }, true)
                }
            }
        })
    }
}