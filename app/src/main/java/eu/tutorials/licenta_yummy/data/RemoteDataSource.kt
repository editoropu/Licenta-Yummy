package eu.tutorials.licenta_yummy.data

import eu.tutorials.licenta_yummy.data.network.FoodRecipesApi
import eu.tutorials.licenta_yummy.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(

    private val foodRecipesApi : FoodRecipesApi

) {

    suspend fun getRecipes(queries : Map<String, String>) : Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>) : Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getRecipesByIds(ids: String, apiKey: String): Response<List<eu.tutorials.licenta_yummy.models.Result>> {
        return foodRecipesApi.getRecipesByIds(ids, apiKey)
    }

}