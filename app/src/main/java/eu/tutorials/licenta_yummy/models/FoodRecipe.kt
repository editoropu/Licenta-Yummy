package eu.tutorials.licenta_yummy.models
import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val results: ArrayList <Result>



)
