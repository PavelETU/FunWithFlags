package com.wordpress.lonelytripblog.funwithflags.data;

import androidx.lifecycle.LiveData;

import com.wordpress.lonelytripblog.funwithflags.data.db.Country;

/**
 * Created by Павел on 09.03.2018.
 */

public interface GameRepo {
    LiveData<GameEntity> getLiveDataForGame();
    void nextFlag();
    void saveCurrentFlagIntoLearntFlags();
    LiveData<Integer> getAmountOfLeftFlags();
    LiveData<Country> getLearntFlag();
    void nextLearntFlag();
    LiveData<Integer> getAmountOfLearntFlags();
}
