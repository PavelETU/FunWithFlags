package com.wordpress.lonelytripblog.funwithflags.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wordpress.lonelytripblog.funwithflags.R;
import com.wordpress.lonelytripblog.funwithflags.data.CallbackForTimer;
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.util.Counter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Павел on 29.12.2017.
 */

public class GameViewModel extends ViewModel implements CallbackForTimer {

    public final ObservableList<Boolean> animateThisItemAsChosen = new ObservableArrayList<>();
    public final ObservableBoolean showRightAnswer = new ObservableBoolean();
    private int lastChosenPosition = -1;

    private GameRepo gameRepository;
    private Counter counter;

    @Inject
    public GameViewModel(@NonNull final GameRepo gameRepo, @NonNull final Counter counter) {
        gameRepository = gameRepo;
        this.counter = counter;
        initVariables();
    }

    private void initVariables() {
        List<Boolean> dullList = new ArrayList<>(Arrays.asList(false, false, false, false));
        animateThisItemAsChosen.addAll(dullList);
        showRightAnswer.set(false);
    }

    public LiveData<GameEntity> getGameEntity() {
        return gameRepository.getLiveDataForGame();
    }

    /*
        Called by databinding upon user click on one of the buttons
     */
    public void getAnswerByUser(int userAnswer) {
        if (lastChosenPosition != userAnswer) {
            if (lastChosenPosition != -1) {
                animateThisItemAsChosen.set(lastChosenPosition, false);
            }
            animateThisItemAsChosen.set(userAnswer, true);
        } else {
            showRightAnswer.set(true);
            counter.startCounter(this);
        }
        lastChosenPosition = userAnswer;
    }

    @Override
    public void doOnTimerStop() {
        resetValues();
        gameRepository.requestNewFlags();
    }

    private void resetValues() {
        showRightAnswer.set(false);
        animateThisItemAsChosen.set(lastChosenPosition, false);
        lastChosenPosition = -1;
    }

    @BindingAdapter(value = {"animate_as_chosen", "animate_as_right_answer"})
    public static void setAnimation(final Button view, final Boolean showAsChosen, final Boolean showAsRightAnswer) {
        Drawable secondDrawable = null;
        if (view == null || showAsChosen == null || showAsRightAnswer == null) return;
        if (showAsChosen && !showAsRightAnswer) {
            secondDrawable = ContextCompat.getDrawable(view.getContext(),
                    R.drawable.btn_background_chosen);
        }
        if (showAsRightAnswer) {
            secondDrawable = ContextCompat.getDrawable(view.getContext(),
                    R.drawable.btn_background_right_answer);
        }
        if (!showAsChosen && !showAsRightAnswer) {
            secondDrawable = ContextCompat.getDrawable(view.getContext(),
                    R.drawable.btn_background);
        }
        if (secondDrawable != null) {
            TransitionDrawable myDraw = new TransitionDrawable(new Drawable[]{view.getBackground(),
                    secondDrawable});
            if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(myDraw);
            } else {
                view.setBackgroundDrawable(myDraw);
            }
            myDraw.startTransition(1000);
        }
    }

    @BindingAdapter("setImage")
    public static void setImageByPicasso(final ImageView view, final String imageUrl) {
        if (imageUrl != null) {
            Picasso.with(view.getContext()).load(imageUrl).into(view);
        }
    }

    public static class GameViewModelFactory implements ViewModelProvider.Factory {

        private final GameRepo gameRepo;
        private final Counter counter;

        @Inject
        public GameViewModelFactory(GameRepo gameRepo, Counter counter) {
            this.gameRepo = gameRepo;
            this.counter = counter;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked overriding")
        public GameViewModel create(@NonNull Class modelClass) {
            return new GameViewModel(gameRepo, counter);
        }
    }


}
