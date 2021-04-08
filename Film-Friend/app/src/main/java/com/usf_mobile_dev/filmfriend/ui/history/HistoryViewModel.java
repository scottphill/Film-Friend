package com.usf_mobile_dev.filmfriend.ui.history;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.data_sources.repository.MovieRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private LiveData<List<MovieListing>> mAllMovies;
    private LiveData<List<MovieListing>> mWatchList;
    private String currentFilter;

    public HistoryViewModel(Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        mAllMovies = movieRepository.getAllMovies();
        mWatchList = movieRepository.getWatchList();
    }

    public LiveData<List<MovieListing>> getAllMovies() { return mAllMovies;}

    public LiveData<List<MovieListing>> getWatchList() { return mWatchList;}

    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    public String getCurrentFilter() { return currentFilter; }

    public void insert(MovieListing movieListing) {movieRepository.insertMovie(movieListing);}
}