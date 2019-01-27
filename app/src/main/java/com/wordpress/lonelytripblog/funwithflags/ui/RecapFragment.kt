package com.wordpress.lonelytripblog.funwithflags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import com.wordpress.lonelytripblog.funwithflags.di.InjectMe
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.RecapViewModel
import kotlinx.android.synthetic.main.fragment_recap.*
import javax.inject.Inject

class RecapFragment : Fragment(), InjectMe {

    private lateinit var viewModel: RecapViewModel

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recap, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecapViewModel::class.java)
        viewModel.getLearntCountries().observe(viewLifecycleOwner, Observer<List<Country>> { countries ->
            countries?.bindToCurrentView() ?: navigateToInfoFragment()
        })
    }

    private fun List<Country>.bindToCurrentView() {
        countries_stack.adapter = StackAdapter(layoutInflater, this)
    }

    private fun navigateToInfoFragment() {
        navigationController.navigateToInfoFragmentAfterAllFlagsWereReviewed()
    }
}
