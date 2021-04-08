package com.usf_mobile_dev.filmfriend.data_sources.local_db;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MatchPreferences;
import com.usf_mobile_dev.filmfriend.utils.RoomTypeConverters;

@Database(
        entities = {MovieListing.class, MatchPreferences.class},
        version = 3,
        exportSchema = false
)
@TypeConverters({RoomTypeConverters.class})
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
    public abstract MatchPreferencesDao matchPreferencesDao();
    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    ///*
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            //new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDao mDao;
        private final MatchPreferencesDao matchPreferencesDao;
        /*String [] titles = {"Shrek", "Shrek 2", "The Lighthouse", "Soul", "Breach", "Ava",
                "Tom & Jerry", "Jumanji", "Monster Hunter", "Grand Isle", "Shutter Island",
                "Crisis", "The Vigil", "Burn It All"};*/

        PopulateDbAsync(MovieRoomDatabase db) {
            mDao = db.movieDao();
            matchPreferencesDao = db.matchPreferencesDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll();
            matchPreferencesDao.deleteAll();

            /*
            for( int i = 0; i <= titles.length - 1; i++) {
                MovieListing movie = new MovieListing(titles[i]);
                mDao.insert(movie);
            }//*/
            return null;
        }
    }
    //*/
}
