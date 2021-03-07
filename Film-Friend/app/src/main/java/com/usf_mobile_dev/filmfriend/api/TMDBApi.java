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

    public static void getMovie(
            Callback<DiscoverResponse> discoverCallback,
            Executor callbackExecutor,
            MatchPreferences preferences
    ){
        TMDBInterface tmdbInterface = TMDBClient
                .getClient(null)
                .create(TMDBInterface.class);

        String releaseYearStart = String.valueOf(
                preferences.getRelease_year_start()
                + "-01-01"
        );

        String releaseYearEnd = String.valueOf(
                preferences.getRelease_year_end()
                        + "-12-31"
        );

        String genresStr = preferences.getGenresString();
        if(preferences.getNumSelectedGenres() >= 17
                || preferences.getNumSelectedGenres() == 0)
            genresStr = null;

        String watchProvidersStr = preferences.getWatchProvidersString();
        if(preferences.getNumSelectedWatchProviders() == 0)
            watchProvidersStr = null;

        Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                API_KEY,
                null,
                releaseYearStart,
                releaseYearEnd,
                preferences.getVote_count_min(),
                preferences.getVote_count_max(),
                Double.valueOf(preferences.getRating_min()),
                Double.valueOf(preferences.getRating_max()),
                genresStr,
                null,
                preferences.getRuntime_min(),
                preferences.getRuntime_max(),
                watchProvidersStr,
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
                int randMoviePage = 1;
                if(numPages > 0)
                    randMoviePage = randGen.nextInt(numPages) + 1;

                String genresStr = preferences.getGenresString();
                if(preferences.getNumSelectedGenres() >= 17
                        || preferences.getNumSelectedGenres() == 0)
                    genresStr = null;

                String watchProvidersStr = preferences.getWatchProvidersString();
                if(preferences.getNumSelectedWatchProviders() == 0)
                    watchProvidersStr = null;

                TMDBInterface tmdbInterface = TMDBClient
                        .getClient(callbackExecutor)
                        .create(TMDBInterface.class);

                Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                        API_KEY,
                        randMoviePage,
                        releaseYearStart,
                        releaseYearEnd,
                        preferences.getVote_count_min(),
                        preferences.getVote_count_max(),
                        Double.valueOf(preferences.getRating_min()),
                        Double.valueOf(preferences.getRating_max()),
                        genresStr,
                        null,
                        preferences.getRuntime_min(),
                        preferences.getRuntime_max(),
                        watchProvidersStr,
                        "US"
                );

                discoverCall.enqueue(discoverCallback);
            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {

            }
        });
    }
}
