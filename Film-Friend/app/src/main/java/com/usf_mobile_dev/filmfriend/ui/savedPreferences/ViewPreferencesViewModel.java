package com.usf_mobile_dev.filmfriend.ui.savedPreferences;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.List;

public class ViewPreferencesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<MatchPreferences>> allMatchPreferences;

    public ViewPreferencesViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMatchPreferences = movieRepository.getAllMatchPreferences();
    }


    public void insertMatchPreferences(MatchPreferences matchPreferences) {
        movieRepository.insertMatchPreference(matchPreferences);
    }

    public void deleteMatchPreferences(String matchPreferencesTitle) {
        // TODO:
        // does nothing yet
    }

    LiveData<List<MatchPreferences>> getAllMatchPreferences() {
        return this.allMatchPreferences;
    }
}
