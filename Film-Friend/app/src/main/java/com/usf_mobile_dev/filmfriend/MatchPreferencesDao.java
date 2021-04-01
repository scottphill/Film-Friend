package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import java.util.List;

@Dao
public interface MatchPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MatchPreferences matchPreferences);

    @Delete
    void deleteMatchPreferences(MatchPreferences matchPreferences);

    @Query("DELETE FROM match_preferences")
    void deleteAll();

    @Query("SELECT * from match_preferences")
    LiveData<List<MatchPreferences>> getAllMatchPreferences();
}
