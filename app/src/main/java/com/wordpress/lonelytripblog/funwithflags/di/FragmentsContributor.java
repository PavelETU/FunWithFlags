package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.ui.AboutFragment;
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment;
import com.wordpress.lonelytripblog.funwithflags.ui.HomeFragment;
import com.wordpress.lonelytripblog.funwithflags.ui.InfoFragment;
import com.wordpress.lonelytripblog.funwithflags.ui.RecapFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsContributor {

    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ContributesAndroidInjector
    abstract GameFragment gameFragment();

    @ContributesAndroidInjector
    abstract RecapFragment recapFragment();

    @ContributesAndroidInjector
    abstract InfoFragment infoFragment();

    @ContributesAndroidInjector
    abstract AboutFragment aboutFragment();

}
