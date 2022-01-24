package com.example.wembleymoviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.wembleymoviesapp.data.MovieApiClient
import com.example.wembleymoviesapp.data.MovieService
import com.example.wembleymoviesapp.data.NetworkHelper
import com.example.wembleymoviesapp.data.RetrofitBuilder
import com.example.wembleymoviesapp.data.database.MovieDao
import com.example.wembleymoviesapp.data.database.MovieDb
import com.example.wembleymoviesapp.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideRetrofit(httpLoggingInterceptor: HttpLoggingInterceptor):
            Retrofit = RetrofitBuilder(httpLoggingInterceptor).createRetrofit()

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper()
}

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit) =
        retrofit.create(MovieApiClient::class.java)

    @Singleton
    @Provides
    fun provideGetMoviesUseCase(service: MovieService, movieDao: MovieDao):
            GetMoviesUseCase = GetMoviesUseCaseImpl(service, movieDao)

    @Singleton
    @Provides
    fun provideFindMovieUseCase(service: MovieService, movieDao: MovieDao):
            FindMovieUseCase = FindMovieUseCaseImpl(service, movieDao)

    @Singleton
    @Provides
    fun provideGetFavMoviesUseCase(movieDao: MovieDao):
            GetFavMoviesUseCase = GetFavMoviesUseCaseImpl(movieDao)

    @Singleton
    @Provides
    fun provideUpdateMoviesFavUseCase(movieDao: MovieDao):
            UpdateMoviesFavUseCase = UpdateMoviesFavUseCaseImpl(movieDao)
}
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideChannelDao(appDatabase: MovieDb): MovieDao {
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext appContext: Context): MovieDb {
        return Room.databaseBuilder(appContext, MovieDb::class.java, "movie-db").build()
    }
}