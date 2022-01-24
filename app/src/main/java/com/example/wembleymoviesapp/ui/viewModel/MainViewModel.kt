package com.example.wembleymoviesapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.wembleymoviesapp.Config
import com.example.wembleymoviesapp.data.RequestResponse
import com.example.wembleymoviesapp.data.model.MovieItem
import com.example.wembleymoviesapp.domain.FindMovieUseCase
import com.example.wembleymoviesapp.domain.GetMoviesUseCase
import com.example.wembleymoviesapp.domain.UpdateMoviesFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val updateMoviesFavUseCase: UpdateMoviesFavUseCase,
    private val findMovieUseCase: FindMovieUseCase
) : ViewModel(), LifecycleObserver {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _lastPage = MutableLiveData<Boolean>()
    val lastPage: LiveData<Boolean>
        get() = _lastPage

    private val _emptyList = MutableLiveData<Boolean>()
    val emptyList: LiveData<Boolean>
        get() = _emptyList

    private val _movies = MutableLiveData<MutableList<MovieItem>>()
    val movies: LiveData<MutableList<MovieItem>>
        get() = _movies

    private val _updatedMovie = MutableLiveData<MovieItem?>()
    val updatedMovie: LiveData<MovieItem?>
        get() = _updatedMovie

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?>
        get() = _errorMsg

    private var fullMovieList: MutableList<MovieItem> = mutableListOf()
    private var lastSearchText: String? = null

    init {
        _isLoading.value = true
        _lastPage.value = false
    }

    fun getMoreItems(page: Int) {
        if (lastSearchText.isNullOrEmpty()) getPopularMovies()
        else findMovieByText(page, lastSearchText!!)
    }

    fun getPopularMovies(page: Int = 1) {
        lastSearchText = null
        viewModelScope.launch {
            when (val response = getMoviesUseCase.getPopularMovies(apiKey = Config.API_KEY, page)) {
                is RequestResponse.Success -> response.value.let {
                    _movies.value = it.toMutableList()
                    if (page == 1)
                        fullMovieList = it.toMutableList()
                    else fullMovieList.addAll(it)

                    _lastPage.value = it.isEmpty()
                    _emptyList.value = page == 1 && it.isEmpty()
                }

                is RequestResponse.GenericError -> {
                    response.error?.let { _errorMsg.value = it }
                }
            }

            _isLoading.value = false
        }
    }

    fun findMovieByText(page: Int = 1, searchText: String?) {
        lastSearchText = searchText
        if (lastSearchText.isNullOrEmpty()) {
            getPopularMovies(page)
        } else {
            viewModelScope.launch {
                when (val response = findMovieUseCase.findMovieUseCase(apiKey = Config.API_KEY, page, lastSearchText!!)) {
                    is RequestResponse.Success -> response.value.let {
                        _movies.value = it.toMutableList()
                        if (page == 1)
                            fullMovieList = it.toMutableList()
                        else fullMovieList.addAll(it)

                        _lastPage.value = it.isEmpty()
                        _emptyList.value = page == 1 && it.isEmpty()
                    }

                    is RequestResponse.GenericError -> {
                        response.error?.let { Log.e("ERROR", it) }
                    }
                }

                _isLoading.value = false
            }
        }
    }

  fun setFavState(movie: MovieItem) {
      viewModelScope.launch {
          val result = updateMoviesFavUseCase.updateMovieFav(movie)
          val item = fullMovieList.find { it.id == result.id }
          item?.isLiked = movie.isLiked
          _updatedMovie.value = item
      }
  }
}