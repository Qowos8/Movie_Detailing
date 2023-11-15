package com.example.homework1.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel: ViewModel() {
    val onButtonStartClicked = MutableLiveData<Unit>()

    fun buttonStart(){
        onButtonStartClicked.value = Unit
    }

}