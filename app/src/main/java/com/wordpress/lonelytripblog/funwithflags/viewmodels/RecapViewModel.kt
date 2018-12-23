package com.wordpress.lonelytripblog.funwithflags.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import javax.inject.Inject

class RecapViewModel @Inject constructor(private val gameRepository: GameRepo): ViewModel() {
    private var learntCountry = gameRepository.getLearntFlag()

    fun getLearntCountry(): LiveData<Country> {
        return learntCountry
    }

    fun requestNewLearntFlag() {
        gameRepository.nextLearntFlag()
    }
}