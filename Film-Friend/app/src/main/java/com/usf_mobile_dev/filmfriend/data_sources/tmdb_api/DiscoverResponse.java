package com.usf_mobile_dev.filmfriend.data_sources.tmdb_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscoverResponse {

    @SerializedName("page")
    public Integer page;
    @SerializedName("results")
    public List<MovieData> movieData = null;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("total_results")
    public Integer totalResults;

    public class MovieData {

        @SerializedName("adult")
        public Boolean adult;
        @SerializedName("backdrop_path")
        public String backdropPath;
        @SerializedName("genre_ids")
        public List<Integer> genreIds = null;
        @SerializedName("id")
        public Integer id;
        @SerializedName("original_language")
        public String originalLanguage;
        @SerializedName("original_title")
        public String originalTitle;
        @SerializedName("overview")
        public String overview;
        @SerializedName("popularity")
        public Double popularity;
        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("release_date")
        public String releaseDate;
        @SerializedName("title")
        public String title;
        @SerializedName("video")
        public Boolean video;
        @SerializedName("vote_average")
        public Double voteAverage;
        @SerializedName("vote_count")
        public Integer voteCount;
    }
}
