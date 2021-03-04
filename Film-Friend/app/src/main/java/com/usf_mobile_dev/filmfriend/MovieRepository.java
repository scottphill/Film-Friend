package com.usf_mobile_dev.filmfriend;

import android.app.Application;

import com.usf_mobile_dev.filmfriend.api.TMDBApi;

public class MovieRepository {
    public MovieRepository(Application application){

    }

    public void getMovie(){
        TMDBApi.getMovie();
    }
}
