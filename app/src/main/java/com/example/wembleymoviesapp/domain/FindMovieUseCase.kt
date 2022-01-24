package com.example.wembleymoviesapp.domain

import com.example.wembleymoviesapp.data.MovieService
import com.example.wembleymoviesapp.data.RequestResponse
import com.example.wembleymoviesapp.data.database.MovieDao
import com.example.wembleymoviesapp.data.model.MovieItem
import javax.inject.Inject

interface FindMovieUseCase {
    suspend fun findMovieUseCase(apiKey: String, page: Int, searchText: String): RequestResponse<List<MovieItem>>
}

class FindMovieUseCaseImpl @Inject constructor(
    private val service: MovieService,
    private val movieDao: MovieDao
): FindMovieUseCase {

    override suspend fun findMovieUseCase(apiKey: String, page: Int, searchText: String): RequestResponse<List<MovieItem>> {
        return when (val response = service.findMovie(apiKey, page, searchText)) {
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