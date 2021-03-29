package com.usf_mobile_dev.filmfriend.ui.history;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.RepositoryCallback;
import com.usf_mobile_dev.filmfriend.RoomCallback;
import com.usf_mobile_dev.filmfriend.ThreadResult;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieListing>> mAllMovies;

    public HistoryViewModel(Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        mAllMovies = new MutableLiveData<>();
        movieRepository.getAllMovies(new RoomCallback() {
            @Override
            public void onComplete(List<MovieListing> result) {
                if(result != null){
                    //Log.d("WATCHLIST", "Result not null");
                    //Log.d("WATCHLIST", String.valueOf(result.getValue().get(0).getWillWatch()));
                    mAllMovies.postValue(result);
                }
            }

            @Override
            public void onComplete(MovieListing result) {

            }
        });
    }

    LiveData<List<MovieListing>> getMovieList() { return mAllMovies;}

    public void getAllWatchList()
    {
        movieRepository.getWatchList(new RoomCallback() {
        @Override
        public void onComplete(List<MovieListing> result) {
            if(result != null){
                //Log.d("WATCHLIST", "Result not null");
                //Log.d("WATCHLIST", String.valueOf(result.getValue().get(0).getWillWatch()));
                mAllMovies.postValue(result);
            }
        }

        @Override
        public void onComplete(MovieListing result) {

        }
        });
    }

    public void getAllMovies(){
        movieRepository.getAllMovies(new RoomCallback() {
            @Override
            public void onComplete(List<MovieListing> result) {
                if(result != null){
                    //Log.d("WATCHLIST", "Result not null");
                    //Log.d("WATCHLIST", String.valueOf(result.getValue().get(0).getWillWatch()));
                    mAllMovies.postValue(result);
                }
            }

            @Override
            public void onComplete(MovieListing result) {

            }
        });
    }

    public void insert(MovieListing movieListing) {movieRepository.insert(movieListing);}
}