package com.example.wembleymoviesapp.domain

import com.example.wembleymoviesapp.data.MovieService
import com.example.wembleymoviesapp.data.RequestResponse
import com.example.wembleymoviesapp.data.database.MovieDao
import com.example.wembleymoviesapp.data.model.MovieItem
import javax.inject.Inject

interface GetMoviesUseCase {
    suspend fun getPopularMovies(apiKey: String, page: Int): RequestResponse<List<MovieItem>>
}

class GetMoviesUseCaseImpl @Inject constructor(
    private val service: MovieService,
    private val movieDao: MovieDao): GetMoviesUseCase {

    override suspend fun getPopularMovies(apiKey: String, page: Int): RequestResponse<List<MovieItem>> {
        return when (val response = service.getMoviesList(apiKey, page)) {
            is RequestResponse.Success -> {
                val likedMovies = movieDao.getAll()
                likedMovies.map { movieDao ->
                    response.value?.results?.find { it.id == movieDao.id }?.isLiked = true
                }

                RequestResponse.Success(response.value?.results ?: emptyList())}
            is RequestResponse.GenericError -> response
            else -> RequestResponse.GenericError(null, "Unknow error")
        }
    }
}