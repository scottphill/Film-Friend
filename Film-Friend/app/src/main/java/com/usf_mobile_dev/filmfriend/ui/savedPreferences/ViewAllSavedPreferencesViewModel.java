package com.usf_mobile_dev.filmfriend.ui.savedPreferences;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.List;

public class ViewAllSavedPreferencesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<MatchPreferences>> allMatchPreferences;

    public ViewAllSavedPreferencesViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMatchPreferences = movieRepository.getAllMatchPreferences();
    }


    public void insertMatchPreferences(MatchPreferences matchPreferences) {
        movieRepository.insertMatchPreferences(matchPreferences);
    }

    public void deleteMatchPreferences(MatchPreferences matchPreferences) {
        movieRepository.deleteMatchPreferences(matchPreferences);
    }

    LiveData<List<MatchPreferences>> getAllMatchPreferences() {
        return this.allMatchPreferences;
    }
}
