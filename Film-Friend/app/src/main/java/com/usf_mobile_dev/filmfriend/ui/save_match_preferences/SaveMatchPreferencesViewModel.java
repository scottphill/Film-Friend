package com.usf_mobile_dev.filmfriend.ui.save_match_preferences;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MatchPreferences;
import com.usf_mobile_dev.filmfriend.data_sources.repository.MovieRepository;

public class SaveMatchPreferencesViewModel extends AndroidViewModel
{

    public static final String INTENT_EXTRAS_MOVIE_PREFERENCES = "com.usf_mobile_dev.filmfriend.intent.extras.movie_preferences";

    private MovieRepository movieRepository;
    private MatchPreferences matchPreferences;

    public SaveMatchPreferencesViewModel(@NonNull Application application) {
        super(application);

        matchPreferences = new MatchPreferences();
        movieRepository = new MovieRepository(application);
    }

    public void saveMatchPreferences(String preferencesTitle) {
        this.matchPreferences.setPreference_title(preferencesTitle);
        this.movieRepository.insertMatchPreferences(matchPreferences);
    }

    public void setMatchPreferences(MatchPreferences matchPreferences){
        this.matchPreferences = matchPreferences;
    }
    public MatchPreferences getMatchPreferences() {
        return this.matchPreferences;
    }
}
