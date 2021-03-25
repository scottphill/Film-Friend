package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MovieListing movieListing);

    @Query("DELETE FROM movies_history")
    void deleteAll();

    @Query("SELECT * from movies_history ORDER BY dateViewed DESC")
    LiveData<List<MovieListing>> getAllMovies();

}
