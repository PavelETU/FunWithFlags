package com.wordpress.lonelytripblog.funwithflags.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.lonelytripblog.funwithflags.R;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;

import javax.inject.Inject;

public class RecapFragment extends Fragment implements InjectableFragment {

    @Inject
    NavigationController navigationController;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recap_frag, container, false);
    }
}
