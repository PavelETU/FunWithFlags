package com.wordpress.lonelytripblog.funwithflags.data

import androidx.lifecycle.LiveData
import com.wordpress.lonelytripblog.funwithflags.data.db.Country

interface GameRepo {
    fun getUnknownCountryGameEntity(): LiveData<GameEntity>
    fun requestNewGameEntity()
    fun saveCurrentFlagIntoLearntFlags()
    fun getAmountOfLeftFlags(): LiveData<Int>
    fun getLearntFlag(): LiveData<Country>
    fun nextLearntFlag()
    fun getAmountOfLearntFlags(): LiveData<Int>
}