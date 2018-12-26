package com.wordpress.lonelytripblog.funwithflags.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.wordpress.lonelytripblog.funwithflags.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView?, imageUrl: Int?) {
        if (view == null || imageUrl == null) return
        view.setImageResource(imageUrl)
    }

    @JvmStatic
    @BindingAdapter("backgroundDrawable")
    fun backgroundDrawable(view: Button, drawableResId: Int) {
        if (drawableResId == 0) return
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