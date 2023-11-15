package com.example.homework1.presentation.factory

import com.example.homework1.data.` api`.ActorApi
import com.example.homework1.presentation.viewModels.DetailsViewModel
import com.example.homework1.data.` api`.DetailApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(private val api: DetailApi, private val actorApi: ActorApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(api, actorApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}