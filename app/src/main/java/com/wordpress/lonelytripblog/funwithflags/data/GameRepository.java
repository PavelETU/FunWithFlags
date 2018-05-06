package com.wordpress.lonelytripblog.funwithflags.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Singleton;

/**
 * Created by Павел on 09.03.2018.
 */

@Singleton
public class GameRepository implements GameRepo {

    private MutableLiveData<GameEntity> result;

    public GameRepository() {
        result = new MutableLiveData<>();
        result.setValue(new GameEntity("http://www.countryflags.io/de/shiny/64.png",
                new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3));
    }

    @Override
    public LiveData<GameEntity> getLiveDataForGame() {
        return result;
    }

    @Override
    public void requestNewFlags() {
        result.setValue(new GameEntity("http://www.countryflags.io/ca/shiny/64.png",
                new ArrayList<>(Arrays.asList("Canada", "USA", "Thailand", "Germany")), 0));
    }

    @Override
    public LiveData<GameEntity> getLearntFlag() {
        return null;
    }
}
