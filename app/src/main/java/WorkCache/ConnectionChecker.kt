package WorkCache

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object ConnectionChecker{
    private val client = OkHttpClient()
    fun isOnline(): Boolean {
        val request = Request.Builder()
            .url("https://developer.themoviedb.org")
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.isSuccessful
            true
        } catch (e: IOException) {
            Log.d("Connection", "Sucks")
            false
        }
    }
}