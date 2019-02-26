package com.wordpress.lonelytripblog.funwithflags.util

import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment
import com.wordpress.lonelytripblog.funwithflags.ui.HomeFragment
import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity
import com.wordpress.lonelytripblog.funwithflags.ui.RecapFragment
import javax.inject.Inject

open class NavigationController @Inject constructor(mainActivity: MainActivity) {

    private val containerId = R.id.main_content
    private val fragmentManager = mainActivity.supportFragmentManager

    fun goToHomeFragmentIfContainerEmpty() {
        if (fragmentManager.findFragmentById(containerId) == null) {
            fragmentManager.beginTransaction().add(containerId, HomeFragment()).commit()
        }
    }

    fun navigateToNewGameFragment() {
        fragmentManager.beginTransaction().replace(containerId, GameFragment()).commit()
    }

    fun navigateToRecapFragment() {
        fragmentManager.beginTransaction().replace(containerId, RecapFragment()).commit()
    }

}