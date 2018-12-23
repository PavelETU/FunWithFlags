package com.wordpress.lonelytripblog.funwithflags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import com.wordpress.lonelytripblog.funwithflags.databinding.RecapFragBinding
import com.wordpress.lonelytripblog.funwithflags.di.InjectMe
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.RecapViewModel
import javax.inject.Inject

class RecapFragment : Fragment(), InjectMe {

    private lateinit var viewModel: RecapViewModel
    private lateinit var recapFragBinding: RecapFragBinding

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recapFragBinding = RecapFragBinding.inflate(inflater, container, false)
        return recapFragBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecapViewModel::class.java)
        recapFragBinding.recapViewModel = viewModel
        viewModel.getLearntCountry().observe(this, Observer<Country> { country ->
            country?.bindToCurrentView() ?: navigateToInfoFragment()
        })
    }

    private fun Country.bindToCurrentView() {
        recapFragBinding.country = this
    }

    private fun navigateToInfoFragment() {
        navigationController.navigateToInfoFragmentAfterAllFlagsWereReviewed()
    }

    override fun onDestroyView() {
        viewModel.getLearntCountry().removeObservers(this)
        super.onDestroyView()
    }
}
