package com.wordpress.lonelytripblog.funwithflags.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.lonelytripblog.funwithflags.databinding.GameFragBinding;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import javax.inject.Inject;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements InjectableFragment {

    public GameViewModel viewModel;
    private GameFragBinding mGameFragBinding;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Inject
    public NavigationController navigationController;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGameFragBinding = GameFragBinding.inflate(inflater, container, false);
        return mGameFragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel.class);
        mGameFragBinding.setGameViewModel(viewModel);
        viewModel.getGameEntity().observe(this, result -> {
            if (result != null) {
                mGameFragBinding.setCountriesList(result.getCountries());
                mGameFragBinding.setCountryImageResource(result.getCountryImageUrl());
                viewModel.setRightAnswer(result.getRightAnswer());
            } else {
                navigationController.navigateToGameInformationFragment(true, false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        viewModel.beforeRemoveObserver();
        viewModel.getGameEntity().removeObservers(this);
        super.onDestroyView();
    }

}
