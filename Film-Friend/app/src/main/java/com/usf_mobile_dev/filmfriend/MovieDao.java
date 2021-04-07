package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieListing movieListing);

    @Update()
    void update(MovieListing movieListing);

    @Update(entity = MovieListing.class)
    void updateDate(MovieListingDate... listingDate);

    @Query("DELETE FROM movies_history")
    void deleteAll();

    @Query("SELECT * from movies_history ORDER BY dateViewed DESC")
    LiveData<List<MovieListing>> getAllMovies();

    @Query("SELECT * from movies_history WHERE willWatch = 1 ORDER BY dateViewed DESC")
    LiveData<List<MovieListing>> getWatchList();

    @Query("SELECT * from movies_history WHERE tmdbMovieId = :id")
    List<MovieListing> getMovie(int id);

}
