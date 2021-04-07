package com.usf_mobile_dev.filmfriend.ui.match;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import com.usf_mobile_dev.filmfriend.utils.IntBoolHashMapToStringConverter;
import com.usf_mobile_dev.filmfriend.utils.StringListToStringConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(tableName = "match_preferences")
public class MatchPreferences implements Serializable {

    // True = all checkboxes start as checked, False = unchecked

    static final private Boolean genre_cb_init = false;
    // True = all checkboxes start as checked, False = unchecked
    static final private Boolean WP_CB_INIT = false;
    //static final private Boolean watch_providers_cb_init = true;

    // The movie preferences
    private int release_year_start = 1850;
    private int release_year_end = 2021;
    private double rating_min = 0;
    private double rating_max = 10;
    private int runtime_min = 0;
    private int runtime_max = 500;
    private int vote_count_min = 0;
    private int vote_count_max = 10000;
    @TypeConverters(IntBoolHashMapToStringConverter.class)
    private HashMap<Integer, Boolean> genres_to_include;
    @TypeConverters(StringListToStringConverter.class)
    private ArrayList<String> included_genres_list;
    @TypeConverters(IntBoolHashMapToStringConverter.class)
    private HashMap<Integer, Boolean> genres_to_exclude;
    @TypeConverters(StringListToStringConverter.class)
    private ArrayList<String> excluded_genres_list;
    @TypeConverters(IntBoolHashMapToStringConverter.class)
    private HashMap<Integer, Boolean> watch_providers_to_include;
    @TypeConverters(StringListToStringConverter.class)
    private ArrayList<String> watch_providers_list;
    private String selected_language_code;
    private String selected_language_name;

    @PrimaryKey
    @NonNull
    private String preference_title;

    // Constructor
    @Ignore
    public MatchPreferences ()
    {
        release_year_start = 1850;
        release_year_end = 2021;
        rating_min = 0;
        rating_max = 10;
        runtime_min = 0;
        runtime_max = 500;
        vote_count_min = 0;
        vote_count_max = 10000000;
        genres_to_include = new HashMap<Integer, Boolean>();
        genres_to_exclude = new HashMap<Integer, Boolean>();
        watch_providers_to_include = new HashMap<Integer, Boolean>();
        included_genres_list = new ArrayList<>();
        excluded_genres_list = new ArrayList<>();
        watch_providers_list = new ArrayList<>();
        watch_providers_to_include.put(8, WP_CB_INIT);
        watch_providers_to_include.put(15, WP_CB_INIT);
        watch_providers_to_include.put(337, WP_CB_INIT);
        watch_providers_to_include.put(9, WP_CB_INIT);
        watch_providers_to_include.put(3, WP_CB_INIT);
        selected_language_code = "en";
        selected_language_name = "English";
        preference_title = "EXAMPLE TITLE";
    }

    // Constructor used for inserting into room
    public MatchPreferences(
            @NonNull
            String preference_title,
            int release_year_start,
            int release_year_end,
            double rating_min,
            double rating_max,
            int runtime_min,
            int runtime_max,
            int vote_count_min,
            int vote_count_max,
            HashMap<Integer, Boolean> genres_to_include,
            HashMap<Integer, Boolean> genres_to_exclude,
            HashMap<Integer, Boolean> watch_providers_to_include,
            ArrayList<String> included_genres_list,
            ArrayList<String> excluded_genres_list,
            ArrayList<String> watch_providers_list,
            String selected_language_code,
            String selected_language_name
    ) {
        this.preference_title = preference_title;
        this.release_year_start = release_year_start;
        this.release_year_end = release_year_end;
        this.rating_min = rating_min;
        this.rating_max = rating_max;
        this.runtime_min = runtime_min;
        this.runtime_max = runtime_max;
        this.vote_count_min = vote_count_min;
        this.vote_count_max = vote_count_max;
        this.genres_to_include = genres_to_include;
        this.genres_to_exclude = genres_to_exclude;
        this.watch_providers_to_include = watch_providers_to_include;
        this.included_genres_list = included_genres_list;
        this.excluded_genres_list = excluded_genres_list;
        this.watch_providers_list = watch_providers_list;
        this.selected_language_code = selected_language_code;
        this.selected_language_name = selected_language_name;
    }

    // Copy Constructor
    public MatchPreferences(MatchPreferences mp) {
        preference_title = mp.getPreference_title();
        release_year_start = mp.getRelease_year_start();
        release_year_end = mp.getRelease_year_end();
        rating_min = mp.getRating_min();
        rating_max = mp.getRating_max();
        runtime_min = mp.getRuntime_min();
        runtime_max = mp.getRuntime_max();
        vote_count_min = mp.getVote_count_min();
        vote_count_max = mp.getVote_count_max();
        genres_to_include = mp.getGenres_to_include();
        genres_to_exclude = mp.getGenres_to_exclude();
        watch_providers_to_include = mp.getWatch_providers_to_include();
        included_genres_list = mp.getIncluded_genres_list();
        excluded_genres_list = mp.getExcluded_genres_list();
        watch_providers_list = mp.getWatch_providers_list();
        /*
        watch_providers_to_include.put(8, WP_CB_INIT);
        watch_providers_to_include.put(15, WP_CB_INIT);
        watch_providers_to_include.put(337, WP_CB_INIT);
        watch_providers_to_include.put(9, WP_CB_INIT);
        watch_providers_to_include.put(3, WP_CB_INIT);
        // */
        selected_language_code = mp.getSelected_language_code();
        selected_language_name = mp.getSelected_language_name();
    }

    public String getIncludedGenresString() {
        return genres_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .collect(Collectors.joining(","));
    }

    public String getExcludedGenresString() {
        return genres_to_exclude.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .collect(Collectors.joining(","));
    }

    public int getNumSelectedIncludedGenres() {
        return (int) genres_to_include.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(entry -> Integer.toString(entry.getKey()))
                .count();
    }

    public int getNumSelectedExcludedGenres() {
        return (int) genres_to_exclude.entrySet().stream()
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
    public HashMap<Integer, Boolean> getGenres_to_exclude() {
        return genres_to_exclude;
    }

    public void setGenres(List<GenreResponse.Genre> genres) {
        this.clearGenres();
        this.addGenres(genres);
    }

    // Adds a genre id to the genre HashMap
    public void addGenre(Integer id) {
        this.genres_to_include.put(id, genre_cb_init);
        this.genres_to_exclude.put(id, genre_cb_init);
    }

    public void addGenres(List<GenreResponse.Genre> genres) {
        for (GenreResponse.Genre genre : genres) {
            this.genres_to_include.put(genre.id, genre_cb_init);
            this.genres_to_exclude.put(genre.id, genre_cb_init);
        }
    }

    // Removes all genres from the genres HashMap
    public void clearGenres() {
        this.genres_to_include.clear();
        this.genres_to_exclude.clear();
    }

    // Setting the bool for a single genre
    public void setIncludedGenre(Integer id, Boolean new_val) {
        this.genres_to_include.replace(id, new_val);
    }

    // Setting the bool for a single genre
    public void setExcludedGenre(Integer id, Boolean new_val) {
        this.genres_to_exclude.replace(id, new_val);
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

    public double getRating_min() { return rating_min; }
    public void setRating_min(double rating_min) { this.rating_min = rating_min; }

    public double getRating_max() { return rating_max; }
    public void setRating_max(double rating_max) { this.rating_max = rating_max; }

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

    public void setSelected_language_code(String selected_language_code) {
        this.selected_language_code = selected_language_code;
    }

    public String getSelected_language_code() {
        return this.selected_language_code;
    }

    public int getRuntime_min() { return runtime_min; }
    public void setRuntime_min(int runtime_min) { this.runtime_min = runtime_min; }

    public int getRuntime_max() { return runtime_max; }
    public void setRuntime_max(int runtime_max) { this.runtime_max = runtime_max; }

    public int getVote_count_min() { return vote_count_min; }
    public void setVote_count_min(int vote_count_min) { this.vote_count_min = vote_count_min; }

    public int getVote_count_max() { return vote_count_max; }
    public void setVote_count_max(int vote_count_max) { this.vote_count_max = vote_count_max; }

    public String getPreference_title() {
        return preference_title;
    }

    public void setPreference_title(String preference_title) {
        this.preference_title = preference_title;
    }

    public String getSelected_language_name() {
        return selected_language_name;
    }

    public void setSelected_language_name(String selected_language_name) {
        this.selected_language_name = selected_language_name;
    }

    public ArrayList<String> getIncluded_genres_list() {
        return included_genres_list;
    }

    public void setIncluded_genres_list(ArrayList<String> included_genres_list) {
        this.included_genres_list = included_genres_list;
    }

    public void addIncludedGenreToList(String genre) {
        this.included_genres_list.add(genre);
    }

    public void removeIncludedGenreFromList(String genre) {
        this.included_genres_list.remove(genre);
    }

    public ArrayList<String> getExcluded_genres_list() {
        return excluded_genres_list;
    }

    public void setExcluded_genres_list(ArrayList<String> excluded_genres_list) {
        this.excluded_genres_list = excluded_genres_list;
    }

    public void addExcludedGenreToList(String genre) {
        this.excluded_genres_list.add(genre);
    }

    public void removeExcludedGenreFromList(String genre) {
        this.excluded_genres_list.remove(genre);
    }

    public ArrayList<String> getWatch_providers_list() {
        return watch_providers_list;
    }

    public void setWatch_providers_list(ArrayList<String> watch_providers_list) {
        this.watch_providers_list = watch_providers_list;
    }

    public void addWatchProviderToList(String watch_provider) {
        this.watch_providers_list.add(watch_provider);
    }

    public void removeWatchProviderFromList(String watch_provider) {
        this.watch_providers_list.remove(watch_provider);
    }
}
