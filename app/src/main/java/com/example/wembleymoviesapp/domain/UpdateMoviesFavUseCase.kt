package com.example.wembleymoviesapp.domain


import com.example.wembleymoviesapp.data.database.MovieDao
import com.example.wembleymoviesapp.data.model.MovieItem
import javax.inject.Inject

interface UpdateMoviesFavUseCase {
    suspend fun updateMovieFav(movie: MovieItem): MovieItem
}

class UpdateMoviesFavUseCaseImpl @Inject constructor(
    private val movieDao: MovieDao
): UpdateMoviesFavUseCase {

    override suspend fun updateMovieFav(movie: MovieItem): MovieItem {
        when(movie.isLiked) {
            true -> {
                movieDao.delete(movie)
                movie.isLiked = false
            }
            false -> {
                movie.isLiked = true
                movieDao.insertOne(movie)
            }
        }

        return movie
    }
}