package Data;
import android.app.Application
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
object AppContextProvider {
    private var appContext: Application? = null

    fun initialize(application: Application) {
        appContext = application
    }

    fun getAppContext(): Application {
        return appContext ?: throw IllegalStateException("AppContextProvider is not initialized")
    }
}

