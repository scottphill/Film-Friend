package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface RoomCallback{
    void onComplete(List<MovieListing> result);
    void onComplete(MovieListing result);
}
