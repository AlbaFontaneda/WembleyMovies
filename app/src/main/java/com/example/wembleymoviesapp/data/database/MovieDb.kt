package com.example.wembleymoviesapp.data.database

import androidx.room.*
import com.example.wembleymoviesapp.data.model.MovieItem

@Database(entities = [MovieItem::class], version = 2)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

@Dao
interface MovieDao {
    @Query("SELECT * from MovieItem")
    suspend fun getAll(): List<MovieItem>
    @Query("SELECT * FROM MovieItem WHERE id = :id")
    suspend fun findById(id: Int): MovieItem
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movies: List<MovieItem>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(movie: MovieItem)
    @Delete
    suspend fun delete(movie: MovieItem)
}