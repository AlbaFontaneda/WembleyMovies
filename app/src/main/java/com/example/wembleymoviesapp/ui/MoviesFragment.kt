package com.example.wembleymoviesapp.ui

import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wembleymoviesapp.Config
import com.example.wembleymoviesapp.data.model.MovieItem
import com.example.wembleymoviesapp.databinding.FragmentMoviesBinding
import com.example.wembleymoviesapp.ui.base.BaseBindingFragment
import com.example.wembleymoviesapp.ui.viewModel.MainViewModel
import com.example.wembleymoviesapp.utils.PaginationScrollListener

class MoviesFragment: BaseBindingFragment<FragmentMoviesBinding, MainViewModel>() {
    override fun getViewBinding(): FragmentMoviesBinding = FragmentMoviesBinding.inflate(layoutInflater)

    override fun isSameViewModelAsActivity(): Boolean = true
    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    private var currentPage = 1
    private lateinit var adapter: MoviesAdapter

    override fun setupView() {
        viewModel.getPopularMovies()

        setFragmentResultListener(Config.UPDATE_MOVIE_REQUEST) { _, bundle ->
            binding.apply {
                adapter.updateItem(bundle.get(Config.UPDATE_MOVIE_KEY) as MovieItem)
            }
        }

        binding.apply {
            val layoutManager = GridLayoutManager(context, 2)
            layoutManager.orientation = GridLayoutManager.VERTICAL
            moviesList.layoutManager = layoutManager

            pullToRefresh.setOnRefreshListener {
                currentPage = 1
                viewModel.getMoreItems(currentPage)
                pullToRefresh.isRefreshing = false
            }

            moviesList.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun loadMoreItems() {
                    currentPage += 1
                    viewModel.getMoreItems(currentPage)
                }

                override fun isLastPage(): Boolean = viewModel.lastPage.value == true
                override fun isLoading(): Boolean = viewModel.isLoading.value == true
            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        currentPage = 1
                        viewModel.getPopularMovies()
                    } else viewModel.findMovieByText(1, newText)

                    return true
                }
            })
        }
        bindData()
    }

    private fun bindData() = with(viewModel) {
        movies.observe(viewLifecycleOwner, { list ->
            list?.let {
                binding.apply {
                    if (currentPage == 1) {
                        adapter = MoviesAdapter(it.toMutableList()) {
                            viewModel.setFavState(it)
                        }
                        moviesList.adapter = adapter
                    } else adapter.addMoreItems(it)
                }
            }
        })

        updatedMovie.observe(viewLifecycleOwner, { list ->
            list?.let {
                binding.apply {
                   adapter.updateItem(it)
                }
            }
        })

        emptyList.observe(viewLifecycleOwner, { isEmpty ->
            binding.apply {
                if (isEmpty) {
                    moviesList.visibility = View.GONE
                    emptyListAdvise.visibility = View.VISIBLE
                } else {
                    moviesList.visibility = View.VISIBLE
                    emptyListAdvise.visibility = View.GONE
                }
            }
        })

        errorMsg.observe(viewLifecycleOwner, { msg ->
            binding.apply {
                if (!msg.isNullOrEmpty())
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            }
        })
    }
}