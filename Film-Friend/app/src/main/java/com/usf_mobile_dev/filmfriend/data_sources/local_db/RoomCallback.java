package com.usf_mobile_dev.filmfriend.data_sources.local_db;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;

import java.util.List;

public interface RoomCallback{
    void onComplete(List<MovieListing> result);
    void onComplete(MovieListing result);
}
