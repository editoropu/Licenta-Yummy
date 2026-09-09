package eu.tutorials.licenta_yummy.data.network
import eu.tutorials.licenta_yummy.models.FoodRecipe
import org.jsoup.helper.HttpConnection

import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.Response

interface FoodRecipesApi {


    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries : Map<String,String>

    ):retrofit2.Response <FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ) : Response<FoodRecipe>

    @GET("/recipes/informationBulk")
    suspend fun getRecipesByIds(
        @retrofit2.http.Query("ids") ids: String,
        @retrofit2.http.Query("apiKey") apiKey: String
    ): Response<List<eu.tutorials.licenta_yummy.models.Result>>

}