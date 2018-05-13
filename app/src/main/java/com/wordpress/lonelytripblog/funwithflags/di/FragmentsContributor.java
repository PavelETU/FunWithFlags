package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment;
import com.wordpress.lonelytripblog.funwithflags.ui.RecapFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsContributor {

    @ContributesAndroidInjector
    abstract GameFragment gameFragment();

    @ContributesAndroidInjector
    abstract RecapFragment recapFragment();

}
