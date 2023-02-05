package com.example.movies_top.api

import com.example.movies_top.model.MovieDetails
import com.example.movies_top.model.MoviesTopList
import com.example.movies_top.utils.Constants.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @Headers("X-API-KEY: $API_KEY")
    @GET("films/top")
    fun getTopMovie(@Query("type") type: String, @Query("page") page: Int): Call<MoviesTopList>
//    fun getTopMovie(@Query("page") page: Int, @Query("X-API-KEY") apiKey: String): Call<MoviesListResponse>
//    fun getTopMovie(@Query("page") page: Int): Call<MoviesListResponse>

    @Headers("X-API-KEY: $API_KEY")
    @GET("films/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Long): Call<MovieDetails>

    @Headers("X-API-KEY: $API_KEY")
    @GET("films/search-by-keyword")
    fun getMoviesByKeyword(@Query("keyword") movieName: String, @Query("page") page: Int): Call<MoviesTopList>
}