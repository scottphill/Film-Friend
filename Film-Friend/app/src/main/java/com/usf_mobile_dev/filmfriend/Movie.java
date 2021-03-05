package com.usf_mobile_dev.filmfriend;

public class Movie {

    private String title;
    private String director;
    private String releaseYear;
    private String overview;

    //NOTE Needs getter member variables for Banner and Poser.
    public Movie(String title, String director, String releaseYear, String overview) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.overview = overview;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getOverview() {
        return overview;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
