package com.example.homework1.data.api

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MovieApiHeaderInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url
            val newUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .addHeader("Authorization", "Bearer $apiKey")
                .build()

            return chain.proceed(newRequest)
        }
    }
class DetailApiHeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(newRequest)
    }
}
class ActorApiHeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(newRequest)
    }
}

object MovieRetrofitModule {
    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(MovieApiHeaderInterceptor(apiKey))
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: MovieApi = retrofit.create(MovieApi::class.java)
}
val movieRetrofit = MovieRetrofitModule.retrofit.newBuilder()
    .client(
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(MovieApiHeaderInterceptor(apiKey))
            .build()
    )
    .build()


object DetailRetrofitModule{
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(DetailApiHeaderInterceptor(apiKey))
            .build()

        private val json = Json {
            ignoreUnknownKeys = true
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiDetailsService: DetailApi = retrofit.create(DetailApi::class.java)
    }

val detailRetrofit = DetailRetrofitModule.retrofit.newBuilder()
    .client(
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(DetailApiHeaderInterceptor(apiKey))
            .build()
    )
    .build()

object ActorRetrofitModule {
    val ActorClient = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(ActorApiHeaderInterceptor(apiKey))
        .build()
    private val json = Json {
        ignoreUnknownKeys = true
    }
    val retrofit: Retrofit = Retrofit.Builder()
        .client(ActorClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiActorService: ActorApi = retrofit.create(ActorApi::class.java)

}
