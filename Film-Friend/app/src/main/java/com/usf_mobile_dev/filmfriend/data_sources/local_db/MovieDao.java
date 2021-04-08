package com.usf_mobile_dev.filmfriend.data_sources.local_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListingDate;

import java.util.List;

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
