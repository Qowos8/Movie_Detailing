package MVVM

import Data.Movie
import androidx.lifecycle.ViewModel
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

class MainViewModel: ViewModel() {
    val onButtonStartClicked = MutableLiveData<Unit>()

    fun buttonStart(){
        onButtonStartClicked.value = Unit
    }

}