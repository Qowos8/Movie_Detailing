package com.example.homework1.presentation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework1.data.ListDatabase
import com.example.homework1.data.api.Api_list
import com.example.homework1.data.api.Api_movie
import com.example.homework1.data.api.MovieApi
import com.example.homework1.data.api.OnMovieClickListener
import com.example.homework1.data.api.apiKey
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ListViewModel(private val apiService: MovieApi) : ViewModel() {
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


    fun loadMoviesRx(context: Context) {
        val response = apiService.getMovies(apiKey)
            .subscribeOn(Schedulers.io())
            .flatMap { movies ->
                Single.fromCallable{
                    initDatabase(context)
                    locationsDb.ListDao().getList(movies.movies)
                    closeDatabase()
                    movieList.postValue(movies.movies)
                    status = 1
                    movies.movies
                }.subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext{ throwable ->

                    getOnlineOrOffline(2)
                    initDatabase(context)
                    if (listEntity.isEmpty()) {
                        listEntity = locationsDb.ListDao().insertList()
                    }
                    movieList.postValue(listEntity)
                    status = 2
                    Single.just(listEntity)
                    .doOnSuccess { closeDatabase() }

            }
            .subscribe { movies ->
                status = 1
            }

    }
    /*fun loadMoviesOnBackgroundRx(context: Context) {
        val response = apiService.getMovies(apiKey)
            .subscribeOn(Schedulers.io())
            .flatMap { movies ->
                Single.fromCallable{
                    initDatabase(context)
                    locationsDb.ListDao().getList(movies.movies)
                    closeDatabase()
                }.subscribeOn(Schedulers.io())
                    .map { movies }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext {throwable ->
                Log.e("loadMoviesOnBackgroundRx", "Error in network request: ${throwable.message}")
                Single.fromCallable { getMoviesFromDatabase(context) }
            }
            .subscribe { movies ->
                status = 1
                movieList.postValue(movies.movies)
            }

    }

    fun loadMoviesOnBackground(context: Context) {

        viewModelScope.launch {
            try {
                if (listEntity.isEmpty()) {
                    getOnlineOrOffline(1)
                    status = 1
                    val response = apiService.getMovies(apiKey)
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        if (movieResponse != null) {
                            val listmovies = movieResponse.movies
                            movieList.value = listmovies
                            initDatabase(context)
                            locationsDb.ListDao().getList(listmovies)
                            closeDatabase()
                            status = 1
                        } else {
                            Log.d("ListViewModel", "Error getMovies: Response body is null")
                        }
                    } else {
                        Log.d("ListViewModel", "Error getMovies: ${response.code}")
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
                if (listEntity.isEmpty())
                    listEntity = withContext(Dispatchers.IO) {
                        locationsDb.ListDao().insertList()
                    }
                val apiDetails = listEntity
                movieList.value = apiDetails
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
        with(WorkManager.getInstance(context)) {
            enqueueUniquePeriodicWork("TEST_BACK", ExistingPeriodicWorkPolicy.REPLACE, requestik)
        }
    }*/
    private fun getMoviesFromDatabase(context: Context): List<Api_list>{
        return emptyList()
    }
    fun navigateToMovie(movieId: Int) {
        _navigateToMovie.value = movieId
    }

    fun onMovieClicked(movie: Api_movie, movieId: Int) {
        movieClickListener?.onMovieClicked(movie, movieId)
    }

    fun onDetailClicked(movie: Api_movie) {
        movieClickListener?.onMovieClicked(movie, movie.id)
    }
}