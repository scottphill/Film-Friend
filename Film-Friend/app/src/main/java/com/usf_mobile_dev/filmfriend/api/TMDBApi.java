package com.usf_mobile_dev.filmfriend.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBApi {
    public static String API_KEY = "25ace50f784640868b88295ea133e67e";

    public static String getMovie(){
        TMDBInterface tmdbInterface = TMDBClient.getClient().create(TMDBInterface.class);

        Call<DiscoverResponse> discoverCall = tmdbInterface.discoverMovie(
                API_KEY,
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
                null
        );

        discoverCall.enqueue(new Callback<DiscoverResponse>() {
            @Override
            public void onResponse(Call<DiscoverResponse> call, Response<DiscoverResponse> response) {
                DiscoverResponse results = response.body();
                String resultsStr = results.movieData.get(0).toString();
                Log.d("MOVIE_RESULT", resultsStr);
            }

            @Override
            public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                call.cancel();
            }
        });

        //discoverCall.

        return "lol";
    }
}
