package com.example.homework1.presentation.adapters
import com.example.homework1.data.` api`.Api_details
import com.example.homework1.data.` api`.OnMovieClickListener
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.util.Log
import com.example.homework1.data.` api`.BASE_IMAGE_URL
import com.example.homework1.R
import com.example.homework1.data.` api`.apiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import com.example.homework1.data.` api`.DetailRetrofitModule.apiDetailsService
import android.widget.Button
import android.widget.ProgressBar

class DetailAdapter(private val context: Context, private val detail: List<Api_details>): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var onDetailClickListener: OnMovieClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.details_default, parent, false)

        return DetailViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position.toInt()
    }
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        val details = detail[position]
        val fullImageUrl: String? = BASE_IMAGE_URL + details.poster_path
        CoroutineScope(Dispatchers.IO).launch{
            loadDetailDataFromNetwork(holder, details.id)
            Log.d("DetailAdapter", "Received ${detail.size} details")
        }
        if (fullImageUrl != null) {
            loadImage(fullImageUrl, holder.detailPoster)
        }
        //if (holder.isDataLoaded) {
            holder.progressBar.visibility = View.VISIBLE
            holder.detailName.text = details.original_title
            holder.detailRating.text = details.vote_average.toString()
            holder.detailStoryline.text = details.overview
            holder.budget.text = details.budget
            holder.release.text = details.release_data
            holder.ageRate.text = details.runtime
            holder.language.text = details.original_language
            holder.progressBar.visibility = View.GONE
            Log.d("DetailAdapter", "Received ${detail.size} details")

        holder.runtime_word.visibility = View.VISIBLE
        holder.min_word.visibility = View.VISIBLE
        holder.rate_word.visibility = View.VISIBLE
        holder.percent_symbol.visibility = View.VISIBLE
        holder.budget_word.visibility = View.VISIBLE
        holder.storyline_word.visibility = View.VISIBLE
        holder.cast_word.visibility = View.VISIBLE
        holder.release_word.visibility = View.VISIBLE
        holder.language_word.visibility = View.VISIBLE
        //holder.button_back.visibility = View.VISIBLE


        //}
        /*else{
            holder.progressBar.visibility = View.VISIBLE
        }*/
    }

    override fun getItemCount(): Int = detail.size


    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*val detailName: TextView = itemView.findViewById(R.id.movie_name_text)
        val detailPoster: ImageView = itemView.findViewById(R.id.avengers_png)
        val detailRating: TextView = itemView.findViewById(R.id.rate_bar_avengers)
        val detailStoryline: TextView = itemView.findViewById(R.id.storyline_avengers)
        val ageRate: TextView = itemView.findViewById(R.id.runtime)
        val budget: TextView = itemView.findViewById(R.id.budget)
        val language: TextView = itemView.findViewById((R.id.language))
        val release: TextView = itemView.findViewById(R.id.release_date)*/

        var isDataLoaded = false
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val detailName: TextView = itemView.findViewById(R.id.movie_name_text_default)
        val detailPoster: ImageView = itemView.findViewById(R.id.png_default)
        val detailRating: TextView = itemView.findViewById(R.id.rate_bar_default)
        val detailStoryline: TextView = itemView.findViewById(R.id.storyline_default)
        val ageRate: TextView = itemView.findViewById(R.id.runtime_default)
        val budget: TextView = itemView.findViewById(R.id.budget)
        val language: TextView = itemView.findViewById((R.id.language))
        val release: TextView = itemView.findViewById(R.id.release_date)

        val runtime_word: TextView = itemView.findViewById(R.id.time)
        val min_word: TextView = itemView.findViewById((R.id.min))
        val rate_word: TextView = itemView.findViewById((R.id.rating))
        val percent_symbol: TextView = itemView.findViewById((R.id.percent))
        val budget_word: TextView = itemView.findViewById((R.id.budgett))
        val storyline_word: TextView = itemView.findViewById((R.id.storyline))
        val cast_word: TextView = itemView.findViewById((R.id.cast_word))
        val release_word: TextView = itemView.findViewById((R.id.release_word))
        val language_word: TextView = itemView.findViewById(R.id.original_language)
        val button_back: Button = itemView.findViewById(R.id.back_btn)




    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun loadDetailDataFromNetwork(holder: DetailViewHolder, movieId: Int) {
        val handler = Handler(Looper.getMainLooper())
        try {
            val response = apiDetailsService.getDetails(movieId, apiKey)
            if (response.isSuccessful) {
                val detailList = response.body()
                if (detailList != null) {
                    val fullImageUrl = BASE_IMAGE_URL + detailList.poster_path
                    try {
                        handler.post {
                            Glide.with(context)
                                .load(fullImageUrl)
                                .placeholder(R.drawable.background)
                                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                                .into(holder.detailPoster)
                        }

                    } catch (e: Exception) {
                        Log.d("Poster:", "Error with image")
                    }
                    //holder.release.text = detailList.release_data
                    //holder.budget.text = detailList.budget
                    //holder.detailName.text = detailList.original_title
                    //holder.detailGenre.text = detailList.genres
                    //holder.detailRating.text = detailList.vote_average.toString()
                    //holder.detailStoryline.text = detailList.overview
                    //holder.detailReview.text = detailList.release_data
                    //holder.ageRate.text = detailList.runtime
                }
            }
        }
        catch (e: Exception){
            Log.d("ListAdapter", "Exception")
        }
    }
    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.background)
            .into(imageView)
    }



}