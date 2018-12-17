package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.FunWithFlagApp;
import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {ViewModelModule.class,
        AndroidInjectionModule.class,
        ActivityContributor.class,
        ViewModelsProvider.class})
public interface AppMainComponent {

    void injectApp(FunWithFlagApp app);

}
