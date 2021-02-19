package com.usf_mobile_dev.filmfriend.ui.home;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usf_mobile_dev.filmfriend.MovieRepository;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;

    public HomeViewModel(Application application) {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        movieRepository = new MovieRepository(application);
    }

    public LiveData<String> getText() {
        return mText;
    }
}