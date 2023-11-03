package WorkCache

import DB.ListDatabase
import Data.MovieRetrofitModule.apiService
import android.content.Context
import androidx.work.WorkerParameters
import com.example.homework1.apiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*class CoroutineWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private lateinit var locationDb: ListDatabase
    override suspend fun doWork(context: Context): Result {
        locationDb = ListDatabase.newInstance(context.applicationContext)
        withContext(Dispatchers.IO){
            val response = apiService.getMovies(apiKey)
            if(response.isSuccessful){
                val movieResponse = response.body()
                if(moiveResponse != null){
                    val listMovies = movieResponse?.movies
                    if (listMovies != null) {
                        locationDb.ListDao().getList(listMovies)
                    }
                    locationDb.close()
                }
            }
        }
        return Result.success()
    }


}*/