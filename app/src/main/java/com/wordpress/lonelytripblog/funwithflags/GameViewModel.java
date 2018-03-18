package com.wordpress.lonelytripblog.funwithflags;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Павел on 29.12.2017.
 */

public class GameViewModel extends ViewModel {

    public final ObservableList<Boolean> animateThisItemAsChosen = new ObservableArrayList<>();
    public final ObservableBoolean showRightAnswer = new ObservableBoolean();
    private int lastChosenPosition = -1;

    private GameRepo gameRepository;

    @Inject
    public GameViewModel(@NonNull final GameRepo gameRepo) {
        gameRepository = gameRepo;
        initVariables();
    }

    private void initVariables() {
        List<Boolean> dullList = new ArrayList<>(Arrays.asList(false, false, false, false));
        animateThisItemAsChosen.addAll(dullList);
        showRightAnswer.set(false);
    }

    public LiveData<GameEntity> getGameEntity() {
        return gameRepository.getNewFlag();
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
        }
        lastChosenPosition = userAnswer;
    }

    @BindingAdapter(value = {"animate_as_chosen", "animate_as_right_answer"}, requireAll = false)
    public static void setAnimation(final Button view, final Boolean showAsChosen, final Boolean showAsRightAnswer) {
        Drawable secondDrawable = null;
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
//        view.setBackgroundResource(R.drawable.btn_background_chosen);
//        Animation fadeIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
//        view.startAnimation(fadeIn);
//        fadeIn.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        AnimationDrawable animation = (AnimationDrawable)
//                (view.getBackground() instanceof AnimationDrawable ? view.getBackground() : null);
//        if (animation != null) {
//            animation.setEnterFadeDuration(500);
//            animation.setExitFadeDuration(500);
//            animation.start();
//        }
//        if (showAsRightAnswer != null && showAsRightAnswer) {
//            view.setBackgroundResource(R.drawable.btn_background_right_answer);
//        }
    }

    @BindingAdapter("setImage")
    public static void setImageByPicasso(final ImageView view, final String imageUrl) {
        if (imageUrl != null) {
            Picasso.with(view.getContext()).load(imageUrl).into(view);
        }
    }

    public static class GameViewModelFactory implements ViewModelProvider.Factory {

        private final GameRepo gameRepo;

        @Inject
        public GameViewModelFactory(GameRepo gameRepo) {
            this.gameRepo = gameRepo;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked overriding")
        public GameViewModel create(@NonNull Class modelClass) {
            return new GameViewModel(gameRepo);
        }
    }


}
