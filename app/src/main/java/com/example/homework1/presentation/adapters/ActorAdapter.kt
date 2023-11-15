package com.example.homework1.presentation.adapters

import com.example.homework1.domain.entity.Actor
import com.example.homework1.data.` api`.ActorRetrofitModule.apiActorService
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework1.data.` api`.BASE_IMAGE_URL
import com.example.homework1.R
import com.example.homework1.data.` api`.apiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Handler
import com.bumptech.glide.Glide
import com.example.homework1.data.` api`.BASE_URL

class ActorAdapter(private val context: Context, private val actors: List<Actor>): RecyclerView.Adapter<ActorAdapter.ActorViewHolder>(){


    class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val actorName: TextView = itemView.findViewById(R.id.name_actor)
        val actorRole: TextView = itemView.findViewById(R.id.role)
        val actorImage: ImageView = itemView.findViewById(R.id.portrait)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.actor_default, parent, false)
        return ActorViewHolder(view)
    }

    override fun getItemCount(): Int = actors.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]
        //val Cast = cast
        val fullImageUrl = BASE_IMAGE_URL + actor.profile_path
        Log.d("full", fullImageUrl)
        CoroutineScope(Dispatchers.IO).launch {
            loadActorDataFromNetwork(holder, actor.id)
        }
        loadImage(fullImageUrl, holder.actorImage)
        holder.actorName.text = actor.name
        holder.actorRole.text = actor.role

    }
    @SuppressLint("SuspiciousIndentation")
    private suspend fun loadActorDataFromNetwork(holder: ActorViewHolder, movieId: Int){
        val handler = Handler(Looper.getMainLooper())
            try {
                val response = apiActorService.getActor(movieId, apiKey)
                if(response.isSuccessful) {
                    val actorList = response.body()
                    if (actorList != null) {
                        val Cast = actorList.cast
                        for (Actor in Cast) {
                            val profilePath = Actor.profile_path
                            val fullImageUrl = BASE_URL + profilePath
                            Log.d("fullimage", "$fullImageUrl")
                            try{
                                handler.post{
                                    Glide.with(context)
                                        .load(fullImageUrl)
                                        .placeholder(R.drawable.bg)
                                        .into(holder.actorImage)
                                }
                            }catch (e:Exception){
                                Log.d("actorAdapter", "${e.message}}")
                            }
                        }

                    }
                }
            }catch (e:Exception){
                Log.d("ActorAdapter", "Exception with loading ${e.message}")
            }
    }
    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)
    }
}