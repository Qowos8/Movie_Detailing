package Factory

import Data.MovieRetrofitModule
import MVVM.ListViewModel
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
/*class ListFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            val movieApi = OkhttpModule.OkhttpService
            return ListViewModel(movieApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/