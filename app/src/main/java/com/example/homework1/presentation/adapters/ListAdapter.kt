package com.example.homework1.presentation.adapters

import com.example.homework1.data.api.Api_movie
import com.example.homework1.data.api.MovieRetrofitModule.apiService
import com.example.homework1.data.api.OnMovieClickListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework1.data.api.BASE_IMAGE_URL
import com.example.homework1.presentation.MovieListFragment
import com.example.homework1.R
import com.example.homework1.data.api.apiKey
import com.google.android.material.card.MaterialCardView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieAdapter(private val context: Context, private val movies: List<Api_movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var onMovieClickListener: OnMovieClickListener? = null

    fun setOnClickListener(listener: MovieListFragment) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.view_holder_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        val fullImageUrl = BASE_IMAGE_URL + movie.poster_path
        CoroutineScope(Dispatchers.IO).launch {
            loadMovieDataFromNetwork(holder, movie.id)
        }
        loadImage(fullImageUrl, holder.movieImage)
        holder.movieName.text = movie.original_title
        holder.movieRating.text = movie.vote_average.toString()
        holder.movieTime.text = movie.release_date
        holder.itemView.setOnClickListener {
            Log.d("MovieAdapter", "Clicked on movie: ${movie.original_title}, id: ${movie.id}")
            onMovieClickListener?.onMovieClicked(movie, movie.id)
        }
        holder.card.setOnClickListener {
            onMovieClickListener?.onMovieClicked(movie, movie.id)
        }
    }


    override fun getItemCount(): Int = movies.size


    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.name)
        val movieTime: TextView = itemView.findViewById(R.id.date)
        val movieRating: TextView = itemView.findViewById(R.id.rate)
        val movieImage: ImageView = itemView.findViewById(R.id.picture)
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)

    }

    /*@SuppressLint("SuspiciousIndentation")
    private suspend fun loadMovieDataFromNetwork1(holder: MovieViewHolder, movieId: Int) {
        val handler = Handler(Looper.getMainLooper())
        try {
            val response = apiService.getMovies(apiKey)
            if (response.isSuccessful) {
                val apiList = response.body()
                if (apiList != null) {
                    val movie = apiList.movies.firstOrNull { it.id == movieId }
                    if (movie != null) {
                        val fullImageUrl = BASE_IMAGE_URL + movie.poster_path
                        handler.post {
                            Glide.with(context)
                                .load(fullImageUrl)
                                .into(holder.movieImage)
                            holder.movieName.text = movie.original_title
                            holder.movieRating.text = movie.vote_average.toString()
                            holder.movieTime.text = movie.release_date
                            holder.itemView.setOnClickListener {
                                Log.d(
                                    "MovieAdapter",
                                    "Clicked on movie: ${movie.original_title}, id: $movieId"
                                )
                                try {
                                    onMovieClickListener?.onMovieClicked(movie, movieId)
                                } catch (e: Exception) {
                                    Log.d("ListAdapter", "${e.message}. $e")
                                }
                            }
                        }
                    }
                }
            } else {
                Log.d("ListAdapter", "Response not successful")
            }
        } catch (e: Exception) {

            Log.d("ListAdapter", "Exception: ${e.message}б $e")
        }
    }*/

    private fun loadMovieDataFromNetwork(holder: MovieViewHolder, movieId: Int) {
        val result = apiService.getMovies(apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movie ->
                Single.fromCallable {
                    val movie = movie.movies.firstOrNull { it.id == movieId }
                    val fullImageUrl = BASE_IMAGE_URL + movie?.poster_path

                    Glide.with(context)
                        .load(fullImageUrl)
                        .into(holder.movieImage)
                    holder.movieName.text = movie?.original_title
                    holder.movieRating.text = movie?.vote_average.toString()
                    holder.movieTime.text = movie?.release_date
                    holder.itemView.setOnClickListener {
                        Log.d(
                            "MovieAdapter",
                            "Clicked on movie: ${movie?.original_title}, id: $movieId"
                        )
                        try {
                            onMovieClickListener?.onMovieClicked(movie!!, movieId)
                        } catch (e: Exception) {
                            Log.d("ListAdapter", "${e.message}. $e")
                        }
                    }
                }
            }
    }


    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }
}



