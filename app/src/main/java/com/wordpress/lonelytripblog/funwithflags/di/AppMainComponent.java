package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Павел on 09.03.2018.
 */

@Singleton
@Component(modules = {ViewModelModule.class,
        AndroidInjectionModule.class,
        FragmentsContributor.class})
public interface AppMainComponent {

    void injectToActivityWithGame(MainActivity mainActivity);

}
