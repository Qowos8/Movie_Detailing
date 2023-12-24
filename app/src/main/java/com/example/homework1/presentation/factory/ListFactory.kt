package com.example.homework1.presentation.factory

import com.example.homework1.data.api.MovieRetrofitModule
import com.example.homework1.presentation.viewModels.ListViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            val movieApi = MovieRetrofitModule.apiService
            return ListViewModel(movieApi, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}