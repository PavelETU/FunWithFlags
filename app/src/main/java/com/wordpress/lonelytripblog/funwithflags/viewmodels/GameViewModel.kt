package com.wordpress.lonelytripblog.funwithflags.viewmodels

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.CallbackForTimer
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import java.util.*
import javax.inject.Inject

open class GameViewModel @Inject constructor(private val gameRepository: GameRepo,
                                             private val counter: Counter) : ViewModel(), CallbackForTimer {

    val animateThisItemAsChosen: ObservableList<Boolean> = ObservableArrayList()
    val showAsRightAnswer: ObservableList<Boolean> = ObservableArrayList()
    private var lastChosenPosition = -1
    private var rightAnswerPosition = -1

    open val gameEntity = gameRepository.getUnknownCountryGameEntity()

    init {
        initVariables()
    }

    private fun initVariables() {
        val dullList = ArrayList(Arrays.asList(false, false, false, false))
        animateThisItemAsChosen.addAll(dullList)
        showAsRightAnswer.addAll(dullList)
        amountOfButtonsAnimatedBeforeRotation = 0
    }

    // Workaround to not show buttons animation after rotation
    open fun beforeRemoveObserver() {
        amountOfButtonsAnimatedBeforeRotation =
                if (showAsRightAnswer[0] || showAsRightAnswer[1] || showAsRightAnswer[2]
                        || showAsRightAnswer[3]) {
                    // In case of different chosen and right answer two buttons were animated. Neglect other case for now
                    2
                } else if (lastChosenPosition != -1) {
                    1
                } else {
                    0
                }
    }

    /*
        Called by databinding upon user click on one of the buttons
     */
    fun getAnswerByUser(userAnswer: Int) {
        if (lastChosenPosition != userAnswer) {
            if (lastChosenPosition != -1) {
                animateThisItemAsChosen[lastChosenPosition] = false
            }
            animateThisItemAsChosen[userAnswer] = true
            // User confirms answer
        } else {
            if (userAnswer == rightAnswerPosition) {
                gameRepository.saveCurrentFlagIntoLearntFlags()
            }
            showAsRightAnswer[rightAnswerPosition] = true
            counter.startCounter(this)
        }
        lastChosenPosition = userAnswer
    }

    open fun setRightAnswer(position: Int) {
        rightAnswerPosition = position
    }

    override fun doOnTimerStop() {
        resetValues()
        gameRepository.requestNewGameEntity()
    }

    private fun resetValues() {
        if (lastChosenPosition != -1) {
            animateThisItemAsChosen[lastChosenPosition] = false
            lastChosenPosition = -1
        }
        if (rightAnswerPosition != -1 && showAsRightAnswer[rightAnswerPosition]) {
            showAsRightAnswer[rightAnswerPosition] = false
        }
    }

    companion object {
        private var amountOfButtonsAnimatedBeforeRotation: Int = 0

        @JvmStatic
        @BindingAdapter(value = *arrayOf("animate_as_chosen", "animate_as_right_answer"))
        fun setAnimation(view: Button?, showAsChosen: Boolean?, showAsRightAnswer: Boolean?) {
            var secondDrawable: Drawable? = null
            if (view == null || showAsChosen == null || showAsRightAnswer == null) return
            if (showAsChosen && !showAsRightAnswer) {
                secondDrawable = ContextCompat.getDrawable(view.context,
                        R.drawable.btn_background_chosen)
            }
            if (showAsRightAnswer) {
                secondDrawable = ContextCompat.getDrawable(view.context,
                        R.drawable.btn_background_right_answer)
            }
            if (!showAsChosen && !showAsRightAnswer) {
                secondDrawable = ContextCompat.getDrawable(view.context,
                        R.drawable.btn_background)
            }
            if (secondDrawable != null) {
                // Do not animate button after rotation and during default background setting
                if (amountOfButtonsAnimatedBeforeRotation != 0 || !showAsChosen && !showAsRightAnswer) {
                    setDrawableForImage(view, secondDrawable)
                } else {
                    val myDraw = TransitionDrawable(arrayOf(view.background, secondDrawable))
                    setDrawableForImage(view, myDraw)
                    myDraw.startTransition(1000)
                }
            }
            // Workaround for not showing buttons animation after screen rotation
            if (amountOfButtonsAnimatedBeforeRotation != 0 && (showAsChosen || showAsRightAnswer)) {
                // Handle case when right answer chosen before rotation
                if (amountOfButtonsAnimatedBeforeRotation == 1 || showAsChosen && showAsRightAnswer) {
                    amountOfButtonsAnimatedBeforeRotation = 0
                } else if (amountOfButtonsAnimatedBeforeRotation == 2) {
                    amountOfButtonsAnimatedBeforeRotation--
                }
            }
        }

        private fun setDrawableForImage(view: View, drawableToSet: Drawable) {
            if (Build.VERSION.SDK_INT >= 16) {
                view.background = drawableToSet
            } else {
                view.setBackgroundDrawable(drawableToSet)
            }
        }

        @JvmStatic
        @BindingAdapter("setImage")
        fun setImage(view: ImageView?, imageUrl: Int?) {
            if (view == null || imageUrl == null) return
            view.setImageResource(imageUrl)
        }
    }

}
