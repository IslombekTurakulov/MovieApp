package com.iuturakulov.movieapp.api;

import com.iuturakulov.movieapp.api.model.Genre;
import com.iuturakulov.movieapp.api.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/{category}")
    Call<Movie> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("/3/genre/movie/list")
    Call<Genre> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

}
