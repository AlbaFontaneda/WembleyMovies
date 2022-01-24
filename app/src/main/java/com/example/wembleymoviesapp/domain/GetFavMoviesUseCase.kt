package com.example.wembleymoviesapp.domain

import com.example.wembleymoviesapp.data.database.MovieDao
import com.example.wembleymoviesapp.data.model.MovieItem
import javax.inject.Inject

interface GetFavMoviesUseCase {
    suspend fun getFavMovies(): List<MovieItem>
}

class GetFavMoviesUseCaseImpl @Inject constructor(
    private val movieDao: MovieDao
): GetFavMoviesUseCase {

    override suspend fun getFavMovies(): List<MovieItem> {
       return movieDao.getAll()
    }
}