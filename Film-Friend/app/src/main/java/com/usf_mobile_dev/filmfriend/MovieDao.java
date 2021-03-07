package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(MovieListing movie);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Query("SELECT * from movie_table ORDER BY movieName ASC")
    LiveData<List<MovieListing>> getAllMovies();

}
