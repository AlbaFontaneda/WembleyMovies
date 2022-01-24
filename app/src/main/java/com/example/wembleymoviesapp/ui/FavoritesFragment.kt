package com.example.wembleymoviesapp.ui


import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wembleymoviesapp.Config
import com.example.wembleymoviesapp.databinding.FragmentFavoritesBinding
import com.example.wembleymoviesapp.ui.base.BaseBindingFragment
import com.example.wembleymoviesapp.ui.viewModel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment: BaseBindingFragment<FragmentFavoritesBinding, FavoritesViewModel>() {
    override fun getViewBinding(): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(layoutInflater)

    override fun isSameViewModelAsActivity(): Boolean = false
    override fun getViewModelClass(): Class<FavoritesViewModel> = FavoritesViewModel::class.java

    private lateinit var adapter: MoviesAdapter

    override fun setupView() {
        binding.apply {
            val layoutManager = GridLayoutManager(context, 2)
            layoutManager.orientation = GridLayoutManager.VERTICAL
            moviesList.layoutManager = layoutManager
        }
        bindData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavMovies()
    }

    private fun bindData() = with(viewModel) {
        movies.observe(viewLifecycleOwner, { list ->
            list?.let {
                binding.apply {
                    adapter = MoviesAdapter(it.toMutableList()) {
                        viewModel.setFavState(it)
                    }
                    moviesList.adapter = adapter
                }
            }
        })

        updatedMovie.observe(viewLifecycleOwner, { list ->
            list?.let {
                binding.apply {
                    setFragmentResult(Config.UPDATE_MOVIE_REQUEST,
                        bundleOf(Config.UPDATE_MOVIE_KEY to it))
                    adapter.removeItem(it)
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
    }
}
