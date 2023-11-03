import Data.ActorApi
import com.example.homework1.DetailsViewModel
import Data.DetailApi
import android.content.Context
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