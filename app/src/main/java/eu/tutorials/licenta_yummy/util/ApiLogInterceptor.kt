package eu.tutorials.licenta_yummy.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Scrie in Logcat fiecare cerere catre Spoonacular si raspunsul primit.
 * Filtreaza in Logcat dupa "YummyAPI" ca sa vezi exact ce se intampla.
 */
class ApiLogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d(TAG, "CERERE  -> ${request.url}")

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "CONEXIUNE ESUATA: ${e.javaClass.simpleName} - ${e.message}")
            throw e
        }

        Log.d(TAG, "RASPUNS <- HTTP ${response.code} (${request.url.encodedPath})")
        if (!response.isSuccessful) {
            // peekBody nu consuma raspunsul, deci Retrofit il poate citi in continuare
            val body = response.peekBody(2048).string()
            Log.e(TAG, "CORP EROARE: $body")
        }
        return response
    }

    companion object {
        const val TAG = "YummyAPI"
    }
}
