package com.usf_mobile_dev.filmfriend.ui.match;

import androidx.core.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchPreferences {

    // True = all checkboxes start as checked, False = unchecked
    final private Boolean genre_cb_init = false;
    final private Boolean WP_CB_INIT = false;
    // True = all checkboxes start as checked, False = unchecked
    final private Boolean watch_providers_cb_init = true;

    // The movie preferences
    private int release_year_start = 1850;
    private int release_year_end = 2021;
    private int rating_min = 0;
    private int rating_max = 10;
    private int runtime_min = 0;
    private int runtime_max = 500;
    private int vote_count_min = 0;
    private int vote_count_max = 10000;
    private HashMap<Integer, Boolean> genres_to_include;
    private HashMap<Integer, Boolean> watch_providers_to_include;

    // Constructor
    public MatchPreferences ()
    {
        release_year_start = 1850;
        release_year_end = 2021;
        rating_min = 0;
        rating_max = 10;
        runtime_min = 0;
        runtime_max = 500;
        vote_count_min = 0;
        vote_count_max = 10000;
        // <genre_ids, if_include_in_query>
        genres_to_include = new HashMap<Integer, Boolean>();
        genres_to_include.put(28, genre_cb_init);
        genres_to_include.put(12, genre_cb_init);
        genres_to_include.put(16, genre_cb_init);
        genres_to_include.put(35, genre_cb_init);
        genres_to_include.put(80, genre_cb_init);
        genres_to_include.put(99, genre_cb_init);
        genres_to_include.put(18, genre_cb_init);
        genres_to_include.put(10751, genre_cb_init);
        genres_to_include.put(14, genre_cb_init);
        genres_to_include.put(36, genre_cb_init);
        genres_to_include.put(27, genre_cb_init);
        genres_to_include.put(10402, genre_cb_init);
        genres_to_include.put(9648, genre_cb_init);
        genres_to_include.put(10749, genre_cb_init);
        genres_to_include.put(878, genre_cb_init);
        genres_to_include.put(10770, genre_cb_init);
        genres_to_include.put(53, genre_cb_init);
        //genres_to_include.put(10752, genre_cb_init);
        //genres_to_include.put(37, genre_cb_init);
        // <watch provider ids, if_include_in_query>
        watch_providers_to_include = new HashMap<Integer, Boolean>();
        watch_providers_to_include.put(8, WP_CB_INIT);
        watch_providers_to_include.put(15, WP_CB_INIT);
        watch_providers_to_include.put(337, WP_CB_INIT);
        watch_providers_to_include.put(9, WP_CB_INIT);
        watch_providers_to_include.put(3, WP_CB_INIT);
    }

    public String getGenresString() {
        return genres_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .collect(Collectors.joining(","));
    }

    public int getNumSelectedGenres() {
        return (int) genres_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .count();
    }

    public String getWatchProvidersString() {
        return watch_providers_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .collect(Collectors.joining(","));
    }

    public int getNumSelectedWatchProviders() {
        return (int) watch_providers_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .count();
    }

    // Getters and Setters
    public HashMap<Integer, Boolean> getGenres_to_include() {
        return genres_to_include;
    }
    public void setGenres_to_include(HashMap<Integer, Boolean> genres_to_include) {
        this.genres_to_include = genres_to_include;
    }
    // Setting the bool for a single genre
    public void setGenre(Integer id, Boolean new_val) {
        this.genres_to_include.replace(id, new_val);
    }
    public int getRelease_year_start() {
        return release_year_start;
    }
    public void setRelease_year_start(int release_year_start) {
       this.release_year_start = release_year_start;
    }
    public int getRelease_year_end() {
        return release_year_end;
    }
    public void setRelease_year_end(int release_year_end) {
        this.release_year_end = release_year_end;
    }

    public int getRating_min() { return rating_min; }
    public void setRating_min(int rating_min) { this.rating_min = rating_min; }

    public int getRating_max() { return rating_max; }
    public void setRating_max(int rating_max) { this.rating_max = rating_max; }

    public HashMap<Integer, Boolean> getWatch_providers_to_include() {
        return watch_providers_to_include;
    }
    public void setWatch_providers_to_include(HashMap<Integer, Boolean> watch_providers_to_include) {
        this.watch_providers_to_include = watch_providers_to_include;
    }
    // Setting the bool for a single watch provider
    public void setWatchProvider(Integer id, Boolean new_val) {
        this.watch_providers_to_include.replace(id, new_val);
    }

    public int getRuntime_min() { return runtime_min; }
    public void setRuntime_min(int runtime_min) { this.runtime_min = runtime_min; }

    public int getRuntime_max() { return runtime_max; }
    public void setRuntime_max(int runtime_max) { this.runtime_max = runtime_max; }

    public int getVote_count_min() { return vote_count_min; }
    public void setVote_count_min(int vote_count_min) { this.vote_count_min = vote_count_min; }

    public int getVote_count_max() { return vote_count_max; }
    public void setVote_count_max(int vote_count_max) { this.vote_count_max = vote_count_max; }
}
