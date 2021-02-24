package com.usf_mobile_dev.filmfriend.ui.match;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;

    public MatchViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Match fragment");
        movieRepository = new MovieRepository(application);
    }

    public LiveData<String> getText() {
        return mText;
    }
}