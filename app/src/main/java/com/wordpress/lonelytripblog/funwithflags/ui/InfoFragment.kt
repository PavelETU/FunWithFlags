package com.wordpress.lonelytripblog.funwithflags.ui


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import javax.inject.Inject

const val ALL_FLAGS_LEARNT_FLAG = "all_flags_learnt"

class InfoFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        val forGameFragment = arguments?.getBoolean(ALL_FLAGS_LEARNT_FLAG) ?: false
        viewModel.amountOfLearntAndLeftFlags.observe(this, Observer { it?.also {
            view.flags_info.text = "You have learnt ${it.first} and you have left to learn ${it.second}"
            viewModel.amountOfLearntAndLeftFlags.removeObservers(this)
        }})
        view.continue_btn.visibility =  if (forGameFragment) {
            View.INVISIBLE
        } else {
            view.continue_btn.setOnClickListener { navigationController.navigateToRecapFragment() }
            View.VISIBLE
        }
        return view
    }


}
