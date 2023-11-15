package WorkCache

import com.example.homework1.data.ListDatabase
import com.example.homework1.presentation.factory.ListFactory
import com.example.homework1.presentation.viewModels.ListViewModel
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private lateinit var viewModelStoreOwner: ViewModelStoreOwner
    constructor(context: Context, params: WorkerParameters, viewModelStoreOwnerr: ViewModelStoreOwner) : this(context, params){
        viewModelStoreOwner = viewModelStoreOwnerr
    }
    private lateinit var locationsDb: ListDatabase
    //private lateinit var loadMovies = ListViewModel
    private val myCoroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var app: Application
    val appContext = context.applicationContext as Application
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("MyWorker", "Start work at ${System.currentTimeMillis()}")
            if (appContext is Application) {
                app = appContext as Application
            }
            if (ConnectionChecker.isOnline()) {
                Log.d("MyWorker", "Start work at ${System.currentTimeMillis()}")
                //locationsDb = ListDatabase.newInstance(applicationContext)
                //val mainHandler = Handler(Looper.getMainLooper())
                //mainHandler.post {
                val viewModelFactory = ListFactory(applicationContext)
                val viewModel = ViewModelProvider(
                    ViewModelStore(),
                    viewModelFactory
                )[ListViewModel::class.java]
                //viewModel.initDatabase(applicationContext)
                locationsDb = ListDatabase.newInstance(applicationContext)
                viewModel.loadMoviesOnBackground(applicationContext)
                if(locationsDb != null){
                    Log.d("MyWorker", "Success at ${System.currentTimeMillis()}")
                }
                else{
                    Log.d("MyWorker", "Failure at ${System.currentTimeMillis()}")
                }
                //}
                locationsDb.close()
                //return Result.success()

            } else {
                Log.d("MyWorker", "Fail at ${System.currentTimeMillis()}")
                //return Result.failure()
            }
        }
        return Result.success()
    }
}

