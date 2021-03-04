package com.usf_mobile_dev.filmfriend.ui.match;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    final private Boolean genre_cb_init = true;

    // https://api.themoviedb.org/3/genre/movie/list?api_key=25ace50f784640868b88295ea133e67e&language=en-US
    private HashMap<String, Integer> genres_to_api_id = new HashMap<String, Integer>()
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

    private HashMap<Integer, Boolean> genres_to_include = new HashMap<Integer, Boolean>()
    {{
        put(28, genre_cb_init);
        put(12, genre_cb_init);
        put(16, genre_cb_init);
        put(35, genre_cb_init);
        put(80, genre_cb_init);
        put(99, genre_cb_init);
        put(18, genre_cb_init);
        put(10751, genre_cb_init);
        put(14, genre_cb_init);
        put(36, genre_cb_init);
        put(27, genre_cb_init);
        put(10402, genre_cb_init);
        put(9648, genre_cb_init);
        put(10749, genre_cb_init);
        put(878, genre_cb_init);
        put(10770, genre_cb_init);
        put(53, genre_cb_init);
        put(10752, genre_cb_init);
        put(37, genre_cb_init);
    }};

    public void setGenreVal (String name, Boolean new_val)
    {
        Integer id = genres_to_api_id.get(name);
        genres_to_include.replace(id, new_val);
    }

    public MatchViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Filters:");
        movieRepository = new MovieRepository(application);
    }

    public LiveData<String> getText() {
        return mText;
    }
}