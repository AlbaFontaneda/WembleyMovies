package com.example.wembleymoviesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wembleymoviesapp.Config
import com.example.wembleymoviesapp.R
import com.example.wembleymoviesapp.data.model.MovieItem
import com.example.wembleymoviesapp.databinding.ItemMovieBinding
import com.example.wembleymoviesapp.loadImage

class MoviesAdapter(private val list: MutableList<MovieItem>, val setLikeStatus: (MovieItem) -> Unit)
    : RecyclerView.Adapter<MoviesAdapter.AccreditationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccreditationsViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccreditationsViewHolder(binding)
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AccreditationsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addMoreItems(items: List<MovieItem>) {
        list.addAll(items)
        notifyItemInserted(list.size)
    }

    fun updateItem(movie: MovieItem) {
        val index = list.indexOfFirst { it.id == movie.id }
        list[index] = movie
        notifyItemChanged(index)
    }

    fun removeItem(movie: MovieItem) {
        val index = list.indexOfFirst { it.id == movie.id }
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    inner class AccreditationsViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem) {
            binding.apply {
                movieTitle.text = item.title
                movieCover.loadImage(Config.IMAGE_URL.plus(item.poster))
                likeButton.setOnClickListener {
                    setLikeStatus.invoke(item)
                }

                when (item.isLiked) {
                    true -> likeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                    else -> likeButton.setImageResource(R.drawable.ic_outline_favorite_border_24)
                }
            }
        }
    }
}