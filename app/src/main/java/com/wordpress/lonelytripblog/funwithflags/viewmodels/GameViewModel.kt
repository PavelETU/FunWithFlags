package com.wordpress.lonelytripblog.funwithflags.viewmodels

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.CallbackForTimer
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import javax.inject.Inject

open class GameViewModel @Inject constructor(private val gameRepository: GameRepo,
                                             private val counter: Counter) : ViewModel(), CallbackForTimer {

    open val firstButtonDrawable = MutableLiveData<Int>().apply { value = R.drawable.btn_background }
    open val secondButtonDrawable = MutableLiveData<Int>().apply { value = R.drawable.btn_background }
    open val thirdButtonDrawable = MutableLiveData<Int>().apply { value = R.drawable.btn_background }
    open val fourthButtonDrawable = MutableLiveData<Int>().apply { value = R.drawable.btn_background }
    private var lastChosenPosition = -1
    private var rightAnswerPosition = -1

    open val gameEntity = gameRepository.getUnknownCountryGameEntity()

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

    open fun setRightAnswer(position: Int) {
        rightAnswerPosition = position
    }

    override fun doOnTimerStop() {
        resetValues()
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

    companion object {

        @JvmStatic
        @BindingAdapter("setImage")
        fun setImage(view: ImageView?, imageUrl: Int?) {
            if (view == null || imageUrl == null) return
            view.setImageResource(imageUrl)
        }

        @JvmStatic
        @BindingAdapter("backgroundDrawable")
        fun backgroundDrawable(view: Button, drawableResId: Int) {
            val background = view.background
            if (thisCanBeAnimated(drawableResId) && thisShouldBeAnimated(background)) {
                animateView(background, view, drawableResId)
            } else {
                setDrawableForImage(view, ContextCompat.getDrawable(view.context, drawableResId)!!)
            }
        }

        private fun setDrawableForImage(view: View, drawableToSet: Drawable) {
            if (Build.VERSION.SDK_INT >= 16) {
                view.background = drawableToSet
            } else {
                view.setBackgroundDrawable(drawableToSet)
            }
        }

        private fun thisCanBeAnimated(drawableResId: Int) =
                drawableResId == R.drawable.btn_background_chosen
                        || drawableResId == R.drawable.btn_background_right_answer

        private fun thisShouldBeAnimated(imageDrawable: Drawable) =
                imageDrawable is TransitionDrawable || imageDrawable is GradientDrawable

        private fun animateView(background: Drawable, view: Button, drawableResId: Int) {
            val myDraw = TransitionDrawable(arrayOf(background, ContextCompat.getDrawable(view.context, drawableResId)!!))
            setDrawableForImage(view, myDraw)
            myDraw.startTransition(1000)
        }
    }

}
