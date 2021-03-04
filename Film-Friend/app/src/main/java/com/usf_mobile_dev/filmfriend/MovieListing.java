package com.usf_mobile_dev.filmfriend;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class MovieListing {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieName")
    private String movieName;
    public MovieListing(@NonNull String movieName) {this.movieName = movieName;}

    //Getter
    public String getMovieName() {
        return this.movieName;
    }

    //Setter
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
