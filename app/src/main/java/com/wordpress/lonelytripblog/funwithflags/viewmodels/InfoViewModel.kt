package com.wordpress.lonelytripblog.funwithflags.viewmodels

import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import javax.inject.Inject

class InfoViewModel @Inject constructor(private val gameRepository: GameRepo): ViewModel() {
    val amountOfLearntAndLeftFlags: LiveData<Pair<Int, Int>>
        get() {
            val mediatorLiveData = MediatorLiveData<Pair<Int, Int>>()
            mediatorLiveData.addSource(gameRepository.getAmountOfLeftFlags()) { leftFlags ->
                mediatorLiveData.addSource(gameRepository.getAmountOfLearntFlags()) { learntFlags ->
                    mediatorLiveData.postValue(Pair(learntFlags, leftFlags))
                    mediatorLiveData.removeSource(gameRepository.getAmountOfLeftFlags())
                    mediatorLiveData.removeSource(gameRepository.getAmountOfLearntFlags())
                }
            }
            return mediatorLiveData
        }
}