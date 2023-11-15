package Trash;
import android.app.Application

object AppContextProvider {
    private var appContext: Application? = null

    fun initialize(application: Application) {
        appContext = application
    }

    fun getAppContext(): Application {
        return appContext ?: throw IllegalStateException("AppContextProvider is not initialized")
    }
}

