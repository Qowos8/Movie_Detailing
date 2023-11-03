package com.example.homework1

//import Factory.DetailsViewModelFactory
import DB.DetailDataBase
import Data.Actor
import Data.ActorApi
import Data.Api_movie
import Data.Api_details
import Data.DetailApi
import Data.OnMovieClickListener
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(
    private val api: DetailApi,
    private val actorApi: ActorApi) : ViewModel()  {
    private val movieClick: OnMovieClickListener? = null
    private lateinit var locationsDb: DetailDataBase
    private val _movieDetails = MutableLiveData<List<Api_details>?>()
    private val _actorInfo = MutableLiveData<List<Actor>?>()
    private var listEntity: List<Api_details> = emptyList()
     val movieDetails: MutableLiveData<List<Api_details>?> get() = _movieDetails
    val actorInfo: MutableLiveData<List<Actor>?> get() = _actorInfo
    val _navigateBack = MutableLiveData<Int>()
    private var did: Int = 1
    private val _isLoading = MutableLiveData<Boolean>()
    private lateinit var saves: Unit
    fun initDatabase(context: Context) {
        locationsDb = DetailDataBase.newInstance(context.applicationContext)
    }

    fun closeDatabase() {
        if (::locationsDb.isInitialized) {
            locationsDb.close()
        }
    }
    fun setMovieID(movie_index: Int) {
        did = movie_index
    }

    fun setDetails(context: Context) {
        viewModelScope.launch {
            try {
                //if (listEntity == null || listEntity.isEmpty()) {
                    val detailResponse = api.getDetails(did, apiKey)
                    if (detailResponse.isSuccessful) {
                        val details = detailResponse.body()
                        if (details != null) {
                            _movieDetails.value = listOf(details)
                            /*val saveDetails = listOf(details)
                            initDatabase(context)
                            locationsDb.DetailsDao().getDetail(saveDetails)
                            saves = locationsDb.DetailsDao().getDetail(saveDetails)*/
                        } else {
                            Log.d("DetailViewModel", "Error getDetails: Response body is null")
                        }
                    } else {
                        Log.d("DetailViewModel", "Error getMovies: ${detailResponse.code()}")
                    }
                //}
                /*else{
                    val apiDetails = listEntity
                    Log.d("apiDetails", "$apiDetails")
                    _movieDetails.value = apiDetails
                }*/
            } catch (e: Exception) {
                /*initDatabase(context)
                if (listEntity == null || listEntity.isEmpty())
                    listEntity = withContext(Dispatchers.IO) {
                        locationsDb.DetailsDao().insertDetail()
                    }
                Log.d("listEntity", "$listEntity")

                if (listEntity != null) {
                    val apiDetails = listEntity
                    _movieDetails.value = apiDetails
                } else {
                    _movieDetails.value = emptyList()
                }
                closeDatabase()*/
                Log.e("DetailViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setActor(context: Context){
        viewModelScope.launch {
            try {
                val response = actorApi.getActor(did, apiKey)
                if (response.isSuccessful) {
                    val actorList = response.body()
                    if (actorList != null) {
                        val cast = actorList.cast
                        _actorInfo.value = cast
                    } else {
                        Log.d("setActor", "Error")
                    }
                } else {
                    Log.d("setActor", "response failed")
                }
            }catch (e:Exception){
                Log.d("setActor", "${e.message}")
            }
        }

    }


    fun navigateBack(movieID: Int){
        _navigateBack.value = movieID
    }
    fun onMovieClicked(movie: Api_movie, movieID: Int) {
        movieClick?.onMovieClicked(movie,movieID)
    }

}