package MVVM

import DB.ListDatabase
import DB.MovieEntity
import DB.toApiMovie
import Data.Api_movie
import Data.MovieApi
import Data.OnMovieClickListener
import Data.toMovieEntity
import WorkCache.MyWorker
import WorkCache.Schedule
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.lang.Exception
import com.example.homework1.apiKey
import dagger.hilt.android.internal.Contexts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.concurrent.TimeUnit

class ListViewModel(private val apiService: MovieApi, appContext: Context): ViewModel() {
    private val movieList = MutableLiveData<List<Api_movie>?>()
    private var listEntity: List<Api_movie> = emptyList()
    val movies: LiveData<List<Api_movie>?> get() = movieList
    private val _navigateToMovie = MutableLiveData<Int>()
    private lateinit var locationsDb: ListDatabase
    val navigateToMovie: LiveData<Int> get() = _navigateToMovie
    private var movieClickListener: OnMovieClickListener? = null
    var status: Int? = 0
    //private val _workerCache = MutableLiveData<List>
    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    fun getOnlineOrOffline(status: Int) = status
    fun getOffline() = 2
    fun initDatabase(context: Context) {
        locationsDb = ListDatabase.newInstance(context.applicationContext)
    }

    fun closeDatabase() {
        if (::locationsDb.isInitialized) {
            locationsDb.close()
        }
    }
    fun loadMovies(context: Context) {
        //if (isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    if (listEntity == null || listEntity.isEmpty()) {
                        getOnlineOrOffline(1)
                        status = 1
                        val response = apiService.getMovies(apiKey)
                        if (response.isSuccessful) {
                            val movieResponse = response.body()
                            if (movieResponse != null) {
                                val Listmovies = movieResponse.movies
                                movieList.value = Listmovies
                                initDatabase(context)
                                locationsDb.ListDao().getList(Listmovies)
                                closeDatabase()
                                status = 1
                            } else {
                                Log.d("ListViewModel", "Error getMovies: Response body is null")
                            }
                        } else {
                            Log.d("ListViewModel", "Error getMovies: ${response.code()}")
                        }
                    } else {
                        val apiDetails = listEntity
                        movieList.value = apiDetails
                    }
                } catch (e: Exception) {
                    getOnlineOrOffline(2)
                    status = 2
                    Log.e("ListViewModel", "Exception: ${e.message}", e)
                    initDatabase(context)
                    if (listEntity == null || listEntity.isEmpty())
                        listEntity = withContext(Dispatchers.IO) {
                            locationsDb.ListDao().insertList()
                        }
                    if (listEntity != null) {
                        val apiDetails = listEntity
                        movieList.value = apiDetails
                    } else {
                        movieList.value = emptyList()
                    }
                    status = 2
                    closeDatabase()
                }
            }
    }
    fun loadMoviesOnBackground(context: Context) {

        viewModelScope.launch {
            try {
                if (listEntity == null || listEntity.isEmpty()) {
                    getOnlineOrOffline(1)
                    status = 1
                    val response = apiService.getMovies(apiKey)
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        if (movieResponse != null) {
                            val Listmovies = movieResponse.movies
                            movieList.value = Listmovies
                            initDatabase(context)
                            locationsDb.ListDao().getList(Listmovies)
                            closeDatabase()
                            status = 1
                        } else {
                            Log.d("ListViewModel", "Error getMovies: Response body is null")
                        }
                    } else {
                        Log.d("ListViewModel", "Error getMovies: ${response.code()}")
                    }
                } else {
                    val apiDetails = listEntity
                    movieList.value = apiDetails
                }
            } catch (e: Exception) {
                getOnlineOrOffline(2)
                status = 2
                Log.e("ListViewModel", "Exception: ${e.message}", e)
                initDatabase(context)
                if (listEntity == null || listEntity.isEmpty())
                    listEntity = withContext(Dispatchers.IO) {
                        locationsDb.ListDao().insertList()
                    }
                if (listEntity != null) {
                    val apiDetails = listEntity
                    movieList.value = apiDetails
                } else {
                    movieList.value = emptyList()
                }
                status = 2
                closeDatabase()
            }
        }
        val schedule = Schedule()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
        val constrainedRequest = PeriodicWorkRequest
            .Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        val requestik = PeriodicWorkRequestBuilder<MyWorker>(Duration.ZERO).apply {
            setConstraints(constraints)
            addTag("TEST_BACK")
        }.build()
        with (WorkManager.getInstance(context)){
            enqueueUniquePeriodicWork("TEST_BACK", ExistingPeriodicWorkPolicy.REPLACE, requestik)
        }
        //WorkManager.getInstance(context).enqueue(schedule.constrainedRequest)
    }
    /*companion object{
        fun loadMovies(context: Context) {
            CoroutineScope.launch {
                try {
                        val response = apiService.getMovies(apiKey)
                        if (response.isSuccessful) {
                            val movieResponse = response.body()
                            if (movieResponse != null) {
                                initDatabase(context)
                                locationsDb.ListDao().getList(Listmovies)
                                closeDatabase()
                            } else {
                                Log.d("ListViewModel", "Error getMovies: Response body is null")
                            }
                        } else {
                            Log.d("ListViewModel", "Error getMovies: ${response.code()}")
                        }
                    }
                 catch (e: Exception) {
                    getOnlineOrOffline(2)
                    status = 2
                    Log.e("ListViewModel", "Exception: ${e.message}", e)
                    initDatabase(context)
                    if (listEntity == null || listEntity.isEmpty())
                        listEntity = withContext(Dispatchers.IO) {
                            locationsDb.ListDao().insertList()
                        }
                    if (listEntity != null) {
                        val apiDetails = listEntity
                        movieList.value = apiDetails
                    } else {
                        movieList.value = emptyList()
                    }
                    closeDatabase()
                }
            }
        }
    }*/

    fun navigateToMovie(movieId: Int){
        _navigateToMovie.value = movieId
    }
    fun onMovieClicked(movie: Api_movie, movieId: Int){
        movieClickListener?.onMovieClicked(movie, movieId)
    }
    fun onDetailClicked(movie: Api_movie){
        movieClickListener?.onMovieClicked(movie, movie.id)
    }
}