package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsContributor {

    @ContributesAndroidInjector
    abstract GameFragment gameFragment();

}
