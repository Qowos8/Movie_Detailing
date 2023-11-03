package Adapters
import Data.Api_movie
import Data.MovieRetrofitModule.apiService
import Data.OnMovieClickListener
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
import com.example.homework1.BASE_IMAGE_URL
import com.example.homework1.MovieListFragment
import com.example.homework1.R
import com.example.homework1.apiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieAdapter(private val context: Context, private val movies: List<Api_movie>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var onMovieClickListener: OnMovieClickListener? = null

    fun setOnClickListener(listener: MovieListFragment){
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
            //notifyItemChanged(position)
        }
        loadImage(fullImageUrl, holder.movieImage)
        holder.movieName.text = movie.original_title
        holder.movieRating.text = movie.vote_average.toString()
        holder.movieTime.text = movie.release_date

        holder.itemView.setOnClickListener{
            Log.d("MovieAdapter", "Clicked on movie: ${movie.original_title}, id: ${movie.id}")
            onMovieClickListener?.onMovieClicked(movie, movie.id)
        }
        /*val handler = Handler(Looper.getMainLooper())
        handler.post{
            notifyItemChanged(position)
        }*/

        holder.itemView.setOnClickListener {
            onMovieClickListener?.onMovieClicked(movie, movie.id)
        }
    }



    override fun getItemCount(): Int = movies.size


    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.name)
        val movieTime: TextView = itemView.findViewById(R.id.date)
        //val movieGenre: TextView = itemView.findViewById(R.id.janr)
        val movieRating: TextView = itemView.findViewById(R.id.rate)
        val movieImage: ImageView = itemView.findViewById(R.id.picture)
        /*val movieBg: ImageView = itemView.findViewById(R.id.bg)
        val movieLike: ImageView = itemView.findViewById(R.id.like)
        val movieRectangle: ImageView = itemView.findViewById(R.id.rectangle)
        val movieAge: TextView = itemView.findViewById(R.id.age_rate)*/



    }
    @SuppressLint("SuspiciousIndentation")
    private suspend fun loadMovieDataFromNetwork(holder: MovieViewHolder, movieId: Int) {
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
                                }catch (e:Exception){
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
        }
    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }
    }



