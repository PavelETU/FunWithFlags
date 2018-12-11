package com.wordpress.lonelytripblog.funwithflags.ui;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.lonelytripblog.funwithflags.databinding.RecapFragBinding;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import javax.inject.Inject;

public class RecapFragment extends Fragment implements InjectableFragment {

    private GameViewModel viewModel;
    private RecapFragBinding recapFragBinding;

    @Inject
    public NavigationController navigationController;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recapFragBinding = RecapFragBinding.inflate(inflater, container, false);
        return recapFragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel.class);
        recapFragBinding.setGameViewModel(viewModel);
        viewModel.getLearntCountry().observe(this, country -> {
            if (country == null) {
                navigationController.navigateToInfoFragmentAfterAllFlagsWereReviewed();
            } else {
                recapFragBinding.setCountry(country);
            }
        });
    }

    @Override
    public void onDestroyView() {
        viewModel.getLearntCountry().removeObservers(this);
        super.onDestroyView();
    }
}
