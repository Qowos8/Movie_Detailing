package com.example.homework1.presentation.viewModels

//import Factory.DetailsViewModelFactory
import com.example.homework1.data.DetailDataBase
import com.example.homework1.domain.entity.Actor
import com.example.homework1.data.api.ActorApi
import com.example.homework1.data.api.Api_movie
import com.example.homework1.data.api.Api_details
import com.example.homework1.data.api.DetailApi
import com.example.homework1.data.api.OnMovieClickListener
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework1.data.api.apiKey
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val api: DetailApi,
    private val actorApi: ActorApi
) : ViewModel()  {
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

    fun setDetails() {
        viewModelScope.launch {
            try {
                    val detailResponse = api.getDetails(did, apiKey)
                    if (detailResponse.isSuccessful) {
                        val details = detailResponse.body()
                        if (details != null) {
                            _movieDetails.value = listOf(details)
                        } else {
                            Log.d("DetailViewModel", "Error getDetails: Response body is null")
                        }
                    } else {
                        Log.d("DetailViewModel", "Error getMovies: ${detailResponse.code()}")
                    }

            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}", e)
            }
        }
    }
    /*fun loadDetailsRx() {
        val response = api.getDetails(did)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { details ->
                Single.fromCallable {
                    _movieDetails.postValue(listOf(details))
                }
            }

    }*/
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


    fun navigateBacked(movieID: Int){
        _navigateBack.value = movieID
    }
    fun onMovieClicked(movie: Api_movie, movieID: Int) {
        movieClick?.onMovieClicked(movie,movieID)
    }

}