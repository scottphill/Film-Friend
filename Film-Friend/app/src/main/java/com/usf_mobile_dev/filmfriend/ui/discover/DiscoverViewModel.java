package com.usf_mobile_dev.filmfriend.ui.discover;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private List<Movie> mAllMovies;

    public DiscoverViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Discover fragment");
        movieRepository = new MovieRepository(application);

    }

    public List<Movie> getAllMoviesNearby(List<String> usersNearby) {
        mAllMovies = movieRepository.getAllMoviesNearby(usersNearby);
        return mAllMovies;
    }

    public LiveData<String> getText() {
        return mText;
    }
}