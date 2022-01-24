package com.example.wembleymoviesapp.ui.viewModel

import androidx.lifecycle.*
import com.example.wembleymoviesapp.data.model.MovieItem
import com.example.wembleymoviesapp.domain.GetFavMoviesUseCase
import com.example.wembleymoviesapp.domain.UpdateMoviesFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavMoviesUseCase: GetFavMoviesUseCase,
    private val updateMoviesFavUseCase: UpdateMoviesFavUseCase
) : ViewModel(), LifecycleObserver {

    private val _emptyList = MutableLiveData<Boolean>()
    val emptyList: LiveData<Boolean>
        get() = _emptyList

    private val _movies = MutableLiveData<MutableList<MovieItem>>()
    val movies: LiveData<MutableList<MovieItem>>
        get() = _movies

    private val _updatedMovie = MutableLiveData<MovieItem?>()
    val updatedMovie: LiveData<MovieItem?>
        get() = _updatedMovie


    fun getFavMovies() {
        viewModelScope.launch {
            _movies.value = getFavMoviesUseCase.getFavMovies().toMutableList()
            _emptyList.value = _movies.value?.isEmpty()
        }
    }

    fun setFavState(movie: MovieItem) {
        viewModelScope.launch {
            val result = updateMoviesFavUseCase.updateMovieFav(movie)
            val item = _movies.value?.find { it.id == result.id }
            item?.isLiked = movie.isLiked
            _updatedMovie.value = item
        }
    }
}