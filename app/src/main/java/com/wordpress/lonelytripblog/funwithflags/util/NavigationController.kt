package com.wordpress.lonelytripblog.funwithflags.util

import android.os.Bundle
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

open class NavigationController @Inject constructor(mainActivity: MainActivity) {

    private val containerId = R.id.main_content
    private val fragmentManager = mainActivity.supportFragmentManager
    val callNewGameAsFromNavMenu = {
        mainActivity.navigation_container.setCheckedItem(R.id.new_game)
        navigateToGameInformationFragment(true)
    }
    val callRecapGameAsFromNavMenu = {
        mainActivity.navigation_container.setCheckedItem(R.id.recall_old_flags)
        navigateToRecapFragment()
    }

    fun navigateToFragmentByMenuId(itemId: Int) {
        when (itemId) {
            R.id.home_page -> if (fragmentManager.findFragmentById(containerId) !is HomeFragment) {
                fragmentManager.beginTransaction().replace(containerId, HomeFragment()).commit()
            }
            R.id.new_game -> navigateToGameInformationFragment(true)
            R.id.recall_old_flags -> navigateToGameInformationFragment(false)
            R.id.about_app -> if (fragmentManager.findFragmentById(containerId) !is AboutFragment) {
                fragmentManager.beginTransaction().replace(containerId, AboutFragment()).commit()
            }
        }
    }

    fun navigateToGameInformationFragment(fromGameFragment: Boolean, afterAllFlagsWereReview: Boolean = false) {
        val infoFragment = InfoFragment()
        val bundle = Bundle()
        bundle.putBoolean(FROM_GAME_FRAGMENT, fromGameFragment)
        bundle.putBoolean(AFTER_ALL_FLAGS_WERE_REVIEWED, afterAllFlagsWereReview)
        infoFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(containerId, infoFragment).commit()
    }

    fun navigateToNewGameFragment() {
        fragmentManager.beginTransaction().replace(containerId, GameFragment()).commit()
    }

    fun navigateToRecapFragment() {
        fragmentManager.beginTransaction().replace(containerId, RecapFragment()).commit()
    }

    fun navigateToInfoFragmentAfterAllFlagsWereReviewed() {
        navigateToGameInformationFragment(false, true)
    }

}