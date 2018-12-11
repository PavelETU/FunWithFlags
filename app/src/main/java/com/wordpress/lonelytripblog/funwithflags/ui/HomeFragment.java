package com.wordpress.lonelytripblog.funwithflags.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.lonelytripblog.funwithflags.R;
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;

import javax.inject.Inject;

public class HomeFragment extends Fragment implements InjectableFragment {

    @Inject
    NavigationController navigationController;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.start_game_btn).setOnClickListener(
                v -> navigationController.getCallNewGameAsFromNavMenu().invoke());
        return view;
    }

}
