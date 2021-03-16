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
    void insert(Movie movie);

    @Query("DELETE FROM movies_history")
    void deleteAll();

    @Query("SELECT * from movies_history")
    LiveData<List<Movie>> getAllMovies();

}
