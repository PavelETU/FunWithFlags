package com.wordpress.lonelytripblog.funwithflags.viewmodels

import androidx.lifecycle.*
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.CallbackForTimer
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import javax.inject.Inject

const val GAME_STATE_IN_PROGRESS = 1
const val GAME_STATE_NO_MORE_FLAGS = 2
const val GAME_STATE_TRANSFER = 3

open class GameViewModel @Inject constructor(private val gameRepository: GameRepo,
                                             private val counter: Counter) : ViewModel(), CallbackForTimer {

    open val firstButtonDrawable = MutableLiveData<Int>().apply { postValue(R.drawable.btn_background) }
    open val secondButtonDrawable = MutableLiveData<Int>().apply { postValue(R.drawable.btn_background) }
    open val thirdButtonDrawable = MutableLiveData<Int>().apply { postValue(R.drawable.btn_background) }
    open val fourthButtonDrawable = MutableLiveData<Int>().apply { postValue(R.drawable.btn_background) }
    open val firstButtonText = MutableLiveData<String>()
    open val secondButtonText = MutableLiveData<String>()
    open val thirdButtonText = MutableLiveData<String>()
    open val fourthButtonText = MutableLiveData<String>()
    open val countryImageResId = MutableLiveData<Int>()

    open val gameState = MediatorLiveData<Int>().apply {
        addSource(
                Transformations.map(gameRepository.getUnknownCountryGameEntity()) {
                    it?.parseIntoLiveData()?.let { GAME_STATE_IN_PROGRESS }
                            ?: GAME_STATE_NO_MORE_FLAGS
                }) { value = it }
    }

    private fun GameEntity.parseIntoLiveData(): GameEntity {
        countries.forEachIndexed { index, value -> getValueByIndex(index).value = value }
        setRightAnswer(rightAnswer)
        countryImageResId.value = countryImageUrl
        return this
    }

    private fun getValueByIndex(index: Int): MutableLiveData<String> {
        return when (index) {
            0 -> firstButtonText
            1 -> secondButtonText
            2 -> thirdButtonText
            3 -> fourthButtonText
            else -> throw RuntimeException("Unknown position")
        }
    }

    private var lastChosenPosition = -1
    private var rightAnswerPosition = -1

    /*
        Called by databinding upon user click on one of the buttons
     */
    fun getAnswerByUser(userAnswer: Int) {
        if (lastChosenPosition != userAnswer) {
            if (lastChosenPosition != -1) {
                setResourceToDefault(lastChosenPosition)
            }
            setResourceToChosen(userAnswer)
            // User confirms answer
        } else {
            if (userAnswer == rightAnswerPosition) {
                gameRepository.saveCurrentFlagIntoLearntFlags()
            }
            setResourceToRight(rightAnswerPosition)
            counter.startCounter(this)
        }
        lastChosenPosition = userAnswer
    }

    private fun setResourceToDefault(position: Int) {
        getLiveDataForButtonByPosition(position).value = R.drawable.btn_background
    }

    private fun setResourceToChosen(position: Int) {
        getLiveDataForButtonByPosition(position).value = R.drawable.btn_background_chosen
    }

    private fun setResourceToRight(position: Int) {
        getLiveDataForButtonByPosition(position).value = R.drawable.btn_background_right_answer
    }

    private fun getLiveDataForButtonByPosition(position: Int): MutableLiveData<Int> {
        return when (position) {
            0 -> firstButtonDrawable
            1 -> secondButtonDrawable
            2 -> thirdButtonDrawable
            3 -> fourthButtonDrawable
            else -> throw RuntimeException("Unknown position")
        }
    }

    private fun setRightAnswer(position: Int) {
        rightAnswerPosition = position
    }

    override fun doOnTimerStop() {
        resetValues()
        gameState.value = GAME_STATE_TRANSFER
        gameRepository.requestNewGameEntity()
    }

    private fun resetValues() {
        if (rightAnswerPosition != -1 && lastChosenPosition != rightAnswerPosition) {
            setResourceToDefault(rightAnswerPosition)
        }
        if (lastChosenPosition != -1) {
            setResourceToDefault(lastChosenPosition)
            lastChosenPosition = -1
        }
    }
}
