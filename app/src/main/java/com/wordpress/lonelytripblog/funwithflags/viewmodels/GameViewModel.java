package com.wordpress.lonelytripblog.funwithflags.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wordpress.lonelytripblog.funwithflags.R;
import com.wordpress.lonelytripblog.funwithflags.util.CallbackForTimer;
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
    public final ObservableList<Boolean> showAsRightAnswer = new ObservableArrayList<>();
    private int lastChosenPosition = -1;
    private int rightAnswerPosition = -1;

    private GameRepo gameRepository;
    private Counter counter;
    private static int amountOfButtonsAnimatedBeforeRotation;

    @Inject
    public GameViewModel(@NonNull final GameRepo gameRepo, @NonNull final Counter counter) {
        gameRepository = gameRepo;
        this.counter = counter;
        initVariables();
    }

    private void initVariables() {
        List<Boolean> dullList = new ArrayList<>(Arrays.asList(false, false, false, false));
        animateThisItemAsChosen.addAll(dullList);
        showAsRightAnswer.addAll(dullList);
        amountOfButtonsAnimatedBeforeRotation = 0;
    }

    public LiveData<GameEntity> getGameEntity() {
        return gameRepository.getLiveDataForGame();
    }

    // Workaround to not show buttons animation after rotation
    public void beforeRemoveObserver() {
        if (showAsRightAnswer.get(0) || showAsRightAnswer.get(1) || showAsRightAnswer.get(2) || showAsRightAnswer.get(3)) {
            // In case of different chosen and right answer two buttons were animated. Neglect other case for now
            amountOfButtonsAnimatedBeforeRotation = 2;
        } else if (lastChosenPosition != -1) {
            amountOfButtonsAnimatedBeforeRotation = 1;
        } else {
            amountOfButtonsAnimatedBeforeRotation = 0;
        }
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
        // User confirms answer
        } else {
            if (userAnswer == rightAnswerPosition) {
                gameRepository.saveCurrentFlagIntoLearntFlags();
            }
            showAsRightAnswer.set(rightAnswerPosition, true);
            counter.startCounter(this);
        }
        lastChosenPosition = userAnswer;
    }

    public void setRightAnswer(int position) {
        rightAnswerPosition = position;
    }

    @Override
    public void doOnTimerStop() {
        resetValues();
        gameRepository.nextFlag();
    }

    private void resetValues() {
        if (lastChosenPosition != -1) {
            animateThisItemAsChosen.set(lastChosenPosition, false);
            lastChosenPosition = -1;
        }
        if (rightAnswerPosition != -1 && showAsRightAnswer.get(rightAnswerPosition)) {
            showAsRightAnswer.set(rightAnswerPosition, false);
        }
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
            // Do not animate button after rotation and during default background setting
            if (amountOfButtonsAnimatedBeforeRotation != 0 || (!showAsChosen && !showAsRightAnswer)) {
                setDrawableForImage(view, secondDrawable);
            } else {
                TransitionDrawable myDraw = new TransitionDrawable(new Drawable[]{view.getBackground(),
                        secondDrawable});
                setDrawableForImage(view, myDraw);
                myDraw.startTransition(1000);
            }
        }
        // Workaround for not showing buttons animation after screen rotation
        if (amountOfButtonsAnimatedBeforeRotation != 0 && (showAsChosen || showAsRightAnswer)) {
            // Handle case when right answer chosen before rotation
            if ((amountOfButtonsAnimatedBeforeRotation == 1) || (showAsChosen && showAsRightAnswer)) {
                amountOfButtonsAnimatedBeforeRotation = 0;
            } else if (amountOfButtonsAnimatedBeforeRotation == 2) {
                amountOfButtonsAnimatedBeforeRotation--;
            }
        }
    }

    private static void setDrawableForImage(View view, Drawable drawableToSet) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawableToSet);
        } else {
            view.setBackgroundDrawable(drawableToSet);
        }
    }

    @BindingAdapter("setImage")
    public static void setImage(final ImageView view, final Integer imageUrl) {
        if (view == null || imageUrl == null) return;
        view.setImageResource(imageUrl);
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
