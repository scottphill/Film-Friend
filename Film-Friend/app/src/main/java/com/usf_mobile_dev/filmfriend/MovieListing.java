package com.usf_mobile_dev.filmfriend;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "movie_table")
public class MovieListing {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieName")

    private String movieName;
    private int releaseYear;
    private String posterUrl;

    public MovieListing(@NonNull String movieName, int releaseYear, String posterUrl) {
        this.movieName = movieName;
        this.releaseYear = releaseYear;
        this.posterUrl = posterUrl;
    }

    //Getter
    @NotNull
    public String getMovieName() {
        return movieName;
    }
    public int getReleaseYear() { return releaseYear; }
    public String getPosterUrl() {return posterUrl; }

    //Setter
    public void setMovieName(@NotNull String movieName) {
        this.movieName = movieName;
    }
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear; }
    public void setPosterUrl(String posterUrl) {this.posterUrl = posterUrl;}
}
