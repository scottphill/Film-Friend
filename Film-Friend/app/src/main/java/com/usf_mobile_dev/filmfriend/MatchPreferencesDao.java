package com.usf_mobile_dev.filmfriend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import java.util.List;

@Dao
public interface MatchPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MatchPreferences matchPreferences);

    @Query("DELETE FROM match_preferences")
    void deleteAll();

    @Query("SELECT * from match_preferences")
    LiveData<List<MatchPreferences>> getAllMatchPreferences();
}
