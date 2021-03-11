package com.usf_mobile_dev.filmfriend.ui.discover;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> mAllMovies;

    public DiscoverViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Discover fragment");
        movieRepository = new MovieRepository(application);
        mAllMovies = new MutableLiveData<>();

    }

    void getAllMoviesNearby(List<String> usersNearby) {
        mAllMovies.postValue(movieRepository.getAllMoviesNearby(usersNearby));
    }

    MutableLiveData<List<Movie>> getDiscoverMovieList() {
        return mAllMovies;
    }

    public LiveData<String> getText() {
        return mText;
    }
}