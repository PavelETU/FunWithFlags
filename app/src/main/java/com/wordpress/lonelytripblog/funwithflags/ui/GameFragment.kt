package com.wordpress.lonelytripblog.funwithflags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.databinding.GameFragBinding
import com.wordpress.lonelytripblog.funwithflags.di.InjectMe
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import javax.inject.Inject


class GameFragment : Fragment(), InjectMe {

    private lateinit var viewModel: GameViewModel
    private lateinit var mGameFragBinding: GameFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mGameFragBinding = GameFragBinding.inflate(inflater, container, false)
        return mGameFragBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        mGameFragBinding.gameViewModel = viewModel
        viewModel.gameEntity.observe(this, Observer<GameEntity> { result ->
            result?.bindToUI() ?: navigateToGameInfoFragment()
        })
    }

    private fun GameEntity.bindToUI() {
        mGameFragBinding.countriesList = countries
        mGameFragBinding.countryImageResource = countryImageUrl
        viewModel.setRightAnswer(rightAnswer)
    }

    private fun navigateToGameInfoFragment() {
        navigationController.navigateToGameInformationFragment(true, false)
    }

    override fun onDestroyView() {
        viewModel.beforeRemoveObserver()
        viewModel.gameEntity.removeObservers(this)
        super.onDestroyView()
    }

}
