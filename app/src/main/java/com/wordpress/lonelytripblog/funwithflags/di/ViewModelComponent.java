package com.wordpress.lonelytripblog.funwithflags.di;

import com.wordpress.lonelytripblog.funwithflags.GameFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Павел on 09.03.2018.
 */

@Component(modules = ViewModelModule.class)
public interface ViewModelComponent {

    void injectToFragmentWithGame(GameFragment gameFragment);

}
