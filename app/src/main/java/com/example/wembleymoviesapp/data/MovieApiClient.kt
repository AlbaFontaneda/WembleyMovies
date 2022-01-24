package com.example.wembleymoviesapp.data

import com.example.wembleymoviesapp.data.model.MoviesResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface MovieApiClient {

    @GET("movie/popular")
    suspend fun getMoviesList(@Query("api_key") apiKey: String,
                              @Query("page") page: Int): MoviesResponse?

    @GET("search/movie")
    suspend fun findMovie(@Query("api_key") apiKey: String,
                              @Query("page") page: Int,
                              @Query("query") searchText: String): MoviesResponse?
}

class MovieService @Inject constructor(
    private val api: MovieApiClient,
    private val helper: NetworkHelper) {

    suspend fun getMoviesList(apiKey: String, page: Int): RequestResponse<MoviesResponse?> {
        return helper.safeApiCall(Dispatchers.IO) { api.getMoviesList(apiKey, page) }
    }

    suspend fun findMovie(apiKey: String, page: Int, searchText: String): RequestResponse<MoviesResponse?> {
        return helper.safeApiCall(Dispatchers.IO) { api.findMovie(apiKey, page, searchText) }
    }
}
