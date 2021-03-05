package com.usf_mobile_dev.filmfriend.ui.match;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.RepositoryCallback;
import com.usf_mobile_dev.filmfriend.ThreadResult;
import com.usf_mobile_dev.filmfriend.api.DiscoverResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private MatchPreferences MP;

    public MatchViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Filters:");
        movieRepository = new MovieRepository(application);
        MP = new MatchPreferences();
    }

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
    final private HashMap<String, Integer> watch_providers_to_api_id = new HashMap<String, Integer>()
    {{
        put("Netflix", 8);
        put("Hulu", 15);
        put("Disney+", 337);
        put("Amazon Prime", 9);
        put("Google Play", 3);
    }};

    public void setGenreVal (String name, Boolean new_val)
    {
        Integer id = genres_to_api_id.get(name);
        MP.setGenre(id, new_val);
    }

    public void setWPVal (String name, Boolean new_val)
    {
        Integer id = watch_providers_to_api_id.get(name);
        MP.setWatchProvider(id, new_val);
    }

    public void setRating (int new_rating, boolean is_min)
    {
        if (is_min)
            MP.setRating_min(new_rating);
        else
            MP.setRating_max(new_rating);
    }

    public void setReleaseYear(int year, boolean is_start)
    {
        if (is_start)
            MP.setRelease_year_start(year);
        else
            MP.setRelease_year_end(year);
    }

    public void setRuntime(int mins, boolean is_min)
    {
        if (is_min)
            MP.setRuntime_min(mins);
        else
            MP.setRuntime_max(mins);
    }

    public void setVoteCount(int count, boolean is_min)
    {
        if (is_min)
            MP.setVote_count_min(count);
        else
            MP.setVote_count_max(count);
    }

    public void getTMDBMovie(Context context) {
        movieRepository.getTMDBMovie(
                new Callback<DiscoverResponse>() {
                    @Override
                    public void onResponse(
                            Call<DiscoverResponse> call,
                            Response<DiscoverResponse> response
                    ) {
                        Random randGen = new Random();

                        DiscoverResponse results = response.body();
                        int numMovies = results.movieData.size();
                        int movieChoice = 0;
                        if(numMovies > 0) {
                            movieChoice = randGen.nextInt(numMovies);
                            String resultsStr = results.movieData.get(movieChoice).title;
                            Toast.makeText(context, resultsStr, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(context, "No Matching Movies!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(context, "SEARCH FAILED", Toast.LENGTH_LONG).show();
                    }
                },
                context.getMainExecutor(),
                MP);
            /*new RepositoryCallback<DiscoverResponse>() {
                @Override
                public void onComplete(ThreadResult<DiscoverResponse> result) {
                    if (result instanceof ThreadResult.Success) {
                        // Happy Path
                    }
                    else {
                        // Error Happened - Show to User
                    }
                }
            });*/
    }

    public LiveData<String> getText() {
        return mText;
    }
}