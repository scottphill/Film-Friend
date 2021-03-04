package com.usf_mobile_dev.filmfriend.ui.match;

import android.app.Application;

import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private MatchPreferences MP;

    // https://api.themoviedb.org/3/genre/movie/list?api_key=25ace50f784640868b88295ea133e67e&language=en-US
    final private HashMap<String, Integer> genres_to_api_id = new HashMap<String, Integer>()
    {{
        put("Action", 28);
        put("Adventure", 12);
        put("Animation", 16);
        put("Comedy", 35);
        put("Crime", 80);
        put("Documentary", 99);
        put("Drama", 18);
        put("Family", 10751);
        put("Fantasy", 14);
        put("History", 36);
        put("Horror", 27);
        put("Music", 10402);
        put("Mystery", 9648);
        put("Romance", 10749);
        put("Sci-Fi", 878);
        put("TV Movie", 10770);
        put("Thriller", 53);
        put("War", 10752);
        put("Western", 37);
    }};

    public void setGenreVal (String name, Boolean new_val)
    {
        Integer id = genres_to_api_id.get(name);
        MP.setGenre(id, new_val);
    }

    public void setRating (int new_rating, boolean is_min)
    {
        if (is_min)
            MP.setRating_min(new_rating);
        else
            MP.setRating_max(new_rating);
    }

    public MatchViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Filters:");
        movieRepository = new MovieRepository(application);
        MP = new MatchPreferences();
    }

    public LiveData<String> getText() {
        return mText;
    }
}