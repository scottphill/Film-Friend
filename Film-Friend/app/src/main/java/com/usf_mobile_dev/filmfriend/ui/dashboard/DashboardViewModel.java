package com.usf_mobile_dev.filmfriend.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usf_mobile_dev.filmfriend.MovieRepository;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;

    public DashboardViewModel(Application application) {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        movieRepository = new MovieRepository(application);
    }

    public LiveData<String> getText() {
        return mText;
    }
}