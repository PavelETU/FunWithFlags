package com.wordpress.lonelytripblog.funwithflags.util

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment
import javax.inject.Inject

class GameFragmentFactory @Inject constructor(private val viewModelFactory: ViewModelProvider.Factory,
                                              private val navigationController: NavigationController) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String) =
            when (className) {
                GameFragment::class.java.name -> GameFragment(viewModelFactory, navigationController)
                else -> super.instantiate(classLoader, className)
            }
}