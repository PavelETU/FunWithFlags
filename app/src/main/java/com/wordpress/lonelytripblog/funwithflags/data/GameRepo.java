package com.wordpress.lonelytripblog.funwithflags.data;

import android.arch.lifecycle.LiveData;

/**
 * Created by Павел on 09.03.2018.
 */

public interface GameRepo {
    public LiveData<GameEntity> getNewFlag();
    public LiveData<GameEntity> getLearntFlag();
}
