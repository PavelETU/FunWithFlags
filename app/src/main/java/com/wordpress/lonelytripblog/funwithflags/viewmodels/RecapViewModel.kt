package com.wordpress.lonelytripblog.funwithflags.viewmodels

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import javax.inject.Inject

class RecapViewModel @Inject constructor(gameRepository: GameRepo): ViewModel() {
    private val learntCountries = gameRepository.getLearntFlags()
    private val countryToDisplay: MediatorLiveData<Country> = MediatorLiveData()
    private var pointer = MutableLiveData<Int>()
    var previousBtnVisibility = ObservableInt()
    init {
        countryToDisplay.addSource(learntCountries) {
            countryToDisplay.value = it[pointer.value ?: 0]
        }
        countryToDisplay.addSource(pointer) {
            if (learntCountries.value == null) return@addSource
            countryToDisplay.value = if (learntCountries.value!!.size > it)
                learntCountries.value?.get(it) else null
        }
        pointer.value = 0
        previousBtnVisibility.set(View.INVISIBLE)
    }

    fun getLearntCountry(): LiveData<Country> {
        return countryToDisplay
    }

    fun requestNewLearntFlag() {
        pointer.value = pointer.value!! + 1
        if (pointer.value == 1) previousBtnVisibility.set(View.VISIBLE)
    }

    fun requestPrevLearntFlag() {
        if (pointer.value == 0) return
        pointer.value = pointer.value!! - 1
        if (pointer.value == 0) previousBtnVisibility.set(View.INVISIBLE)
    }
}