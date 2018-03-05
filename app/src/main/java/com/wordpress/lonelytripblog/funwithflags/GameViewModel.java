package com.wordpress.lonelytripblog.funwithflags;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Павел on 29.12.2017.
 */

public class GameViewModel {

    public final ObservableList<String> countriesList = new ObservableArrayList<>();
    public final ObservableList<Boolean> animateThisItemAsChosen = new ObservableArrayList<>();
    public final ObservableList<Boolean> animateThisItemAsRightAnswer = new ObservableArrayList<>();
    public final ObservableField<String> countryImageResource = new ObservableField<>();
    private Integer rightPosition;
    private int lastChosenPosition = -1;

    public GameViewModel(@NonNull final GameEntity gameEntity) {
        countryImageResource.set(gameEntity.getCountryImageUrl());
        countriesList.addAll(gameEntity.getCountries());
        rightPosition = gameEntity.getRightAnswer();
        animateThisItemAsChosen.add(false);
        animateThisItemAsChosen.add(false);
        animateThisItemAsChosen.add(false);
        animateThisItemAsChosen.add(false);
        animateThisItemAsRightAnswer.add(false);
        animateThisItemAsRightAnswer.add(false);
        animateThisItemAsRightAnswer.add(false);
        animateThisItemAsRightAnswer.add(false);
    }

    /*
        Called by databinding upon user click on one of the buttons
     */
    public void getAnswerByUser(int userAnswer) {
        if (lastChosenPosition == userAnswer) {
            if (rightPosition == userAnswer) {
                animateThisItemAsChosen.set(userAnswer, false);
            }
            animateThisItemAsRightAnswer.set(rightPosition, true);
        } else {
            if (lastChosenPosition != -1) {
                animateThisItemAsChosen.set(lastChosenPosition, false);
            }
            lastChosenPosition = userAnswer;
            animateThisItemAsChosen.set(userAnswer, true);
        }
    }

    @BindingAdapter("animated")
    public static void setAnimation(final Button view, final Boolean rightAnswer) {
        if (rightAnswer) {
            view.setBackgroundResource(R.drawable.btn_background_right_answer);
        }
    }

    @BindingAdapter("setImage")
    public static void setImageByPicasso(final ImageView view, final String imageUrl) {
        if (imageUrl != null) {
            Picasso.with(view.getContext()).load(imageUrl).into(view);
        }
    }
}
