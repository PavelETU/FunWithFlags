package com.wordpress.lonelytripblog.funwithflags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.di.InjectMe
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import javax.inject.Inject


class HomeFragment : Fragment(), InjectMe {

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<View>(R.id.start_game_btn).setOnClickListener {
            navigationController.navigateToNewGameFragment()
        }
        return view
    }

}