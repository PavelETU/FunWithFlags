package com.wordpress.lonelytripblog.funwithflags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordpress.lonelytripblog.funwithflags.databinding.GameFragBinding;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GameFragBinding mGameFragBinding = GameFragBinding.inflate(inflater, container, false);

        mGameFragBinding.setViewmodel(new GameViewModel(new GameEntity("http://www.countryflags.io/de/shiny/64.png",
                new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3)));

        View root = mGameFragBinding.getRoot();

        return root;
    }

}
