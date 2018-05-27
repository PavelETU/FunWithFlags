package com.wordpress.lonelytripblog.funwithflags.util

import android.os.Bundle
import com.wordpress.lonelytripblog.funwithflags.OpenForTesting
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.ui.*
import javax.inject.Inject

@OpenForTesting
class NavigationController @Inject constructor(mainActivity: MainActivity) {

    private val containerId = R.id.main_content
    private val fragmentManager = mainActivity.supportFragmentManager

    fun navigateToFragmentByMenuId(itemId: Int) {
        when (itemId) {
            R.id.home_page -> if (fragmentManager.findFragmentById(containerId) !is HomeFragment) {
                fragmentManager.beginTransaction().replace(containerId, HomeFragment()).commit()
            }
            R.id.new_game -> if (fragmentManager.findFragmentById(containerId) !is GameFragment) {
                navigateToGame()
            }
            R.id.recall_old_flags -> if (fragmentManager.findFragmentById(containerId) !is RecapFragment) {
                fragmentManager.beginTransaction().replace(containerId,
                        RecapFragment()).commit()
            }
            R.id.about_app -> if (fragmentManager.findFragmentById(containerId) !is AboutFragment) {
                fragmentManager.beginTransaction().replace(containerId, AboutFragment()).commit()
            }
        }
    }

    fun navigateToGame() {
        fragmentManager.beginTransaction().replace(containerId, GameFragment()).commit()
    }

    fun navigateToGameInformationFragment(allFlagsLearnt: Boolean) {
        val infoFragment = InfoFragment()
        val bundle = Bundle()
        bundle.putBoolean(ALL_FLAGS_LEARNT_FLAG, allFlagsLearnt)
        infoFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(containerId, infoFragment).commit()
    }

}