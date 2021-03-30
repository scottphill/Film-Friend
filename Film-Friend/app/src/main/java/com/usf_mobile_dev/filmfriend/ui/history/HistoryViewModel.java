package com.usf_mobile_dev.filmfriend.ui.history;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private LiveData<List<Movie>> mAllMovies;

    public HistoryViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the History fragment");
        movieRepository = new MovieRepository(application);
        mAllMovies = movieRepository.getAllMovies();

    }

    LiveData<List<Movie>> getAllMovies() {return mAllMovies;}

    public void insert(Movie movie) {movieRepository.insertMovie(movie);}

    public LiveData<String> getText() { return mText;}
}