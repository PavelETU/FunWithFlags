package com.wordpress.lonelytripblog.funwithflags.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import javax.inject.Inject

class RecapViewModel @Inject constructor(private val gameRepository: GameRepo): ViewModel() {

    private val learntCountries = gameRepository.getLearntFlags()

    fun getLearntCountries(): LiveData<List<Country>> {
        return learntCountries
    }
}