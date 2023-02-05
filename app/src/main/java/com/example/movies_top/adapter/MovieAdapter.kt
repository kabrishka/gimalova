package com.example.movies_top.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies_top.ui.MovieDetailsActivity
import com.example.movies_top.databinding.ItemMoviesBinding
import com.example.movies_top.model.MoviesTopList
import com.example.movies_top.ui.MainActivity

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMoviesBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MoviesTopList.Film) {
            binding.apply {
                tvMovieName.text = item.nameRu
                tvInfo.text = item.genres.first().genre.capitalize() + " (" + item.year + ")"
                posterUrlPreview.load(item.posterUrlPreview)
                tvRating.text = item.rating

                root.setOnClickListener {
                    val intent = Intent(context, MovieDetailsActivity::class.java)

                    val fromActivityId = if (context is MainActivity) 0 else 1
                    intent.putExtra("fromActivity", fromActivityId)
                    intent.putExtra("filmId", item.filmId)
                    context.startActivity(intent)
                }
            }

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MoviesTopList.Film>() {
        override fun areItemsTheSame(oldItem: MoviesTopList.Film, newItem: MoviesTopList.Film): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(oldItem: MoviesTopList.Film, newItem: MoviesTopList.Film): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}