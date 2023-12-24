package com.example.homework1.data.api

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class BaseFragment : Fragment() {

    private var coroutineScope = createCoroutineScope()

    override fun onDetach() {
        coroutineScope.cancel("It's time")

        super.onDetach()
    }


    fun getApiKey(): String = apiKey

    fun createCoroutineScope() = CoroutineScope(Job() + Dispatchers.IO)
}



const val BASE_URL: String = "https://api.themoviedb.org/3/"
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
const val API_KEY_HEADER = "bc9810bb6fd465f2587d60057c7d269c"
const val AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYzk4MTBiYjZmZDQ2NWYyNTg3ZDYwMDU3YzdkMjY5YyIsInN1YiI6IjY1MDFkYjEyZGI0ZWQ2MTAzMmE4MDNlMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._xMO7eFog0C64vHv8P5-OX_B6FT1M8aZ-DG4wxZMqkU"
const val apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYzk4MTBiYjZmZDQ2NWYyNTg3ZDYwMDU3YzdkMjY5YyIsInN1YiI6IjY1MDFkYjEyZGI0ZWQ2MTAzMmE4MDNlMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._xMO7eFog0C64vHv8P5-OX_B6FT1M8aZ-DG4wxZMqkU"