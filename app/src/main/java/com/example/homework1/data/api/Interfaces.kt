package com.example.homework1.data.api

import com.example.homework1.domain.entity.cast
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApi {
    @GET("movie/{movie_id}/credits")
    suspend fun getActor(@Path("movie_id") movie_id: Int,
                         @Query("api_key") api_key: String,
                         @Query("language") language: String = "en-US"): Response<cast>
}
interface OnMovieClickListener {

    fun onMovieClicked(movie: Api_movie, movieIndex:Int)

}
interface DetailApi{
    @GET("movie/{movie_id}")
    fun getDetails(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String,
                           @Query("language") language: String = "en-US"): Response<Api_details>

}

interface MovieApi{

    @GET("movie/popular")
    fun getMovies(@Query("api_key") apiKey: String,
                          @Query("language") language: String = "en-US",
                          @Query("page") page: Int = 1): Single<Api_list>
}
