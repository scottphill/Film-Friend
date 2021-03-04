package com.usf_mobile_dev.filmfriend.ui.match;

import androidx.core.util.Pair;

import java.util.HashMap;

public class MatchPreferences {

    // True = all checkboxes start as checked, False = unchecked
    final private Boolean genre_cb_init = true;

    // <genre_ids, if_include_in_query>
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
    private int release_year_start;
    private int release_year_end;

    // Getters and Setters
    public HashMap<Integer, Boolean> getGenres_to_include() {
        return genres_to_include;
    }
    public void setGenres_to_include(HashMap<Integer, Boolean> genres_to_include) {
        this.genres_to_include = genres_to_include;
    }
    public void setGenre(Integer id, Boolean new_val) {
        this.genres_to_include.replace(id, new_val);
    }
    public int getRelease_year_start() {
        return release_year_start;
    }
    public void setRelease_year_start(int release_year_start) {
        if (getRelease_year_end() > release_year_start)
            this.release_year_start = getRelease_year_end();
        else
            this.release_year_start = release_year_start;
    }
    public int getRelease_year_end() {
        return release_year_end;
    }
    public void setRelease_year_end(int release_year_end) {
        if (getRelease_year_start() < release_year_end)
            this.release_year_end = getRelease_year_start();
        else
            this.release_year_end = release_year_end;
    }

    // Constructors
    public MatchPreferences ()
    {

    }
}
