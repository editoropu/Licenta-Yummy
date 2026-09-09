package eu.tutorials.licenta_yummy.util.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.tutorials.licenta_yummy.data.Repository
import eu.tutorials.licenta_yummy.models.FoodRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.tutorials.licenta_yummy.util.ApiErrors
import eu.tutorials.licenta_yummy.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var favoriteRecipesResponse: MutableLiveData<NetworkResult<List<eu.tutorials.licenta_yummy.models.Result>>> = MutableLiveData()
    var foodRecipes : FoodRecipe? = null

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery : Map<String,String>) = viewModelScope.launch {
        serchRecipesSafeCall(searchQuery)
    }

    fun getFavoriteRecipes(ids: String, apiKey: String) = viewModelScope.launch {
        favoriteRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipesByIds(ids, apiKey)
                if (response.isSuccessful && response.body() != null) {
                    favoriteRecipesResponse.value = NetworkResult.Success(response.body()!!)
                } else {
                    // Spunem exact ce a raspuns serverul, nu un mesaj generic
                    favoriteRecipesResponse.value =
                        NetworkResult.Error(ApiErrors.messageFor(response.code(), response.message()))
                }
            } catch (e: Exception) {
                favoriteRecipesResponse.value = NetworkResult.Error(ApiErrors.messageForException(e))
            }
        } else {
            favoriteRecipesResponse.value = NetworkResult.Error("Nu exista conexiune la internet.")
        }
    }

    private suspend fun serchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                foodRecipes = response.body()
                Log.d("resssss",response.body().toString());
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error(ApiErrors.messageForException(e))
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("Nu exista conexiune la internet.")
        }

    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                foodRecipes = response.body()
                Log.d("resssss",response.body().toString());
                recipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error(ApiErrors.messageForException(e))
            }
        } else {
            recipesResponse.value = NetworkResult.Error("Nu exista conexiune la internet.")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        // IMPORTANT: verificam intai daca cererea a esuat (cheie expirata, limita atinsa,
        // fara internet). Codul vechi trata orice esec ca "Recipes not found", asa ca
        // problema reala ramanea invizibila.
        if (!response.isSuccessful) {
            val errorBody = try { response.errorBody()?.string() } catch (e: Exception) { null }
            Log.e("YummyAPI", "HTTP ${response.code()} - $errorBody")
            return NetworkResult.Error(ApiErrors.messageFor(response.code(), response.message()))
        }

        val body = response.body()
        if (body == null) {
            return NetworkResult.Error("Serverul a raspuns fara continut.")
        }

        // Lista goala nu e o eroare: pur si simplu niciun rezultat pentru filtrele alese
        return NetworkResult.Success(body)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}