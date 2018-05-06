package com.wordpress.lonelytripblog.funwithflags.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Павел on 05.03.2018.
 */

public class GameEntity {

    @NonNull
    private Integer countryImageUrl;
    @NonNull
    private List<String> countries = new ArrayList<>(4);
    @NonNull
    private Integer rightAnswer;


    public GameEntity(@NonNull Integer countryImageUrl, @NonNull List<String> countries, @NonNull Integer rightAnswer) {
        this.countryImageUrl = countryImageUrl;
        this.countries = countries;
        this.rightAnswer = rightAnswer;
    }

    @NonNull
    public Integer getCountryImageUrl() {
        return countryImageUrl;
    }

    public void setCountryImageUrl(@NonNull Integer countryImageUrl) {
        this.countryImageUrl = countryImageUrl;
    }

    @NonNull
    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(@NonNull List<String> countries) {
        this.countries = countries;
    }

    @NonNull
    public Integer getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(@NonNull Integer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
