package com.usf_mobile_dev.filmfriend.ui.discover;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private LiveData<List<MovieListing>> mAllMovies;

    public DiscoverViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Discover fragment");
        movieRepository = new MovieRepository(application);
        mAllMovies = movieRepository.getAllMovies();
    }

    LiveData<List<MovieListing>> getAllMovies() {return mAllMovies;}

    public void insert(MovieListing movie) {movieRepository.insert(movie);}

    public LiveData<String> getText() {
        return mText;
    }
}