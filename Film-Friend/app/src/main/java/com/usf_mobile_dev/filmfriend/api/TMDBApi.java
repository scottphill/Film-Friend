package com.usf_mobile_dev.filmfriend.api;

import android.util.Log;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.Random;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBApi {
    public static String API_KEY = "25ace50f784640868b88295ea133e67e";

    public static String getMovie(
            Callback<DiscoverResponse> discoverCallback,
            Executor callbackExecutor,
            MatchPreferences preferences
    ){
        TMDBInterface tmdbInterface = TMDBClient
                .getClient(null)
                .create(TMDBInterface.class);

        Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                API_KEY,
                null,
                null,
                null,
                preferences.getVote_count_min(),
                preferences.getVote_count_max(),
                Double.valueOf(preferences.getRating_min()),
                Double.valueOf(preferences.getRating_max()),
                preferences.getGenresString(),
                null,
                preferences.getRuntime_min(),
                preferences.getRuntime_max(),
                preferences.getWatchProvidersString(),
                "US"
        );

        discoverCall.enqueue(new Callback<DiscoverResponse>() {
            @Override
            public void onResponse(
                    Call<DiscoverResponse> call,
                    Response<DiscoverResponse> response
            ) {
                DiscoverResponse discoverResponse = response.body();

                Random randGen = new Random();
                int numPages = discoverResponse.totalPages;
                int numResults = discoverResponse.totalResults;
                int randMovieIndex = randGen.nextInt(numResults);
                int randMoviePage = (randMovieIndex / numPages) + 1;

                TMDBInterface tmdbInterface = TMDBClient
                        .getClient(callbackExecutor)
                        .create(TMDBInterface.class);

                Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                        API_KEY,
                        randMoviePage,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "US"
                );

                discoverCall.enqueue(discoverCallback);
            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {

            }
        });

        //discoverCall.

        return "lol";
    }
}
