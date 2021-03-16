package com.usf_mobile_dev.filmfriend.api;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBApi {

    public static void getMovie(
            Callback<DiscoverResponse> discoverCallback,
            Executor callbackExecutor,
            MatchPreferences preferences,
            String api_key,
            Set<Integer> viewedPages
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

        String includedGenresString = preferences.getIncludedGenresString();
        if(preferences.getNumSelectedIncludedGenres() >= 17
                || preferences.getNumSelectedIncludedGenres() == 0)
            includedGenresString = null;

        String excludedGenresString = preferences.getExcludedGenresString();
        if(preferences.getNumSelectedExcludedGenres() >= 17
                || preferences.getNumSelectedExcludedGenres() == 0)
            excludedGenresString = null;

        String watchProvidersStr = preferences.getWatchProvidersString();
        if(preferences.getNumSelectedWatchProviders() == 0)
            watchProvidersStr = null;

        String selectedLanguage = preferences.getSelected_language();
        if(selectedLanguage.equals(""))
            selectedLanguage = null;

        Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                api_key,
                null,
                releaseYearStart,
                releaseYearEnd,
                preferences.getVote_count_min(),
                preferences.getVote_count_max(),
                Double.valueOf(preferences.getRating_min()),
                Double.valueOf(preferences.getRating_max()),
                includedGenresString,
                excludedGenresString,
                preferences.getRuntime_min(),
                preferences.getRuntime_max(),
                watchProvidersStr,
                "US",
                selectedLanguage
        );

        discoverCall.enqueue(new Callback<DiscoverResponse>() {
            @Override
            public void onResponse(
                    Call<DiscoverResponse> call,
                    Response<DiscoverResponse> response
            ) {
                DiscoverResponse discoverResponse = response.body();

                int numPages = discoverResponse.totalPages;
                int randMoviePage = 1;
                if(viewedPages != null
                        && viewedPages.size() < numPages
                        && numPages > 0
                ) {
                    List<Integer> shuffledPages = IntStream
                            .rangeClosed(1, numPages)
                            .boxed()
                            .collect(Collectors.toList());
                    Collections.shuffle(shuffledPages);
                    for(int page : shuffledPages) {
                        if(!viewedPages.contains(page)){
                            randMoviePage = page;
                            break;
                        }
                    }
                }
                else if(numPages > 0) {
                    Random randGen = new Random();
                    randMoviePage = randGen.nextInt(numPages) + 1;
                }

                String includedGenresString = preferences.getIncludedGenresString();
                if(preferences.getNumSelectedIncludedGenres() >= 17
                        || preferences.getNumSelectedIncludedGenres() == 0)
                    includedGenresString = null;

                String excludedGenresString = preferences.getExcludedGenresString();
                if(preferences.getNumSelectedExcludedGenres() >= 17
                        || preferences.getNumSelectedExcludedGenres() == 0)
                    excludedGenresString = null;

                String watchProvidersStr = preferences.getWatchProvidersString();
                if(preferences.getNumSelectedWatchProviders() == 0)
                    watchProvidersStr = null;

                String selectedLanguage = preferences.getSelected_language();
                if(selectedLanguage.equals(""))
                    selectedLanguage = null;

                TMDBInterface tmdbInterface = TMDBClient
                        .getClient(callbackExecutor)
                        .create(TMDBInterface.class);

                Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                        api_key,
                        randMoviePage,
                        releaseYearStart,
                        releaseYearEnd,
                        preferences.getVote_count_min(),
                        preferences.getVote_count_max(),
                        Double.valueOf(preferences.getRating_min()),
                        Double.valueOf(preferences.getRating_max()),
                        includedGenresString,
                        excludedGenresString,
                        preferences.getRuntime_min(),
                        preferences.getRuntime_max(),
                        watchProvidersStr,
                        "US",
                        selectedLanguage
                );

                discoverCall.enqueue(discoverCallback);
            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {

            }
        });
    }

    public static void getGenres(
            Callback<GenreResponse> genreCallback,
            Executor callbackExecutor,
            String api_key
    ) {
        TMDBInterface tmdbInterface = TMDBClient
                .getClient(callbackExecutor)
                .create(TMDBInterface.class);

        Call<GenreResponse> genreCall = tmdbInterface.getGenres(
                api_key,
                "en-US"
        );

        genreCall.enqueue(genreCallback);
    }

    public static void getLanguages(
            Callback<List<LanguageResponse>> languageCallback,
            Executor callbackExecutor,
            String api_key
    ) {
        TMDBInterface tmdbInterface = TMDBClient
                .getClient(callbackExecutor)
                .create(TMDBInterface.class);

        Call<List<LanguageResponse>> languageCall = tmdbInterface.getLanguages(
                api_key
        );

        languageCall.enqueue(languageCallback);
    }
}
