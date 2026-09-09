package eu.tutorials.licenta_yummy.util.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import eu.tutorials.licenta_yummy.util.Constants.Companion.API_KEY
import eu.tutorials.licenta_yummy.util.Constants.Companion.MEAL_TYPE
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_API_KEY
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_DIET
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_INSTRUCTIONS_REQUIRED
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_MAX_READY_TIME
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_NUMBER
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_SEARCH
import eu.tutorials.licenta_yummy.util.Constants.Companion.QUERY_TYPE

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    // Functia VECHE (o pastram pentru compatibilitate cu spinner-ul, daca mai e folosita)
    fun applyQueries(selectedMealType: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = "80"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = selectedMealType
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[MEAL_TYPE] = "true"
        return queries
    }

    fun applyQueries2(selectedMealTypes: List<String>): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = "100"
        queries[QUERY_API_KEY] = API_KEY
        val mealTypesString = selectedMealTypes.joinToString(separator = ",")
        queries[QUERY_TYPE] = mealTypesString
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[MEAL_TYPE] = "true"
        return queries
    }

    // FUNCTIA NOUA - aplica filtrele alese de utilizator in FilterActivity
    fun applyFilters(mealType: String, diet: String, maxTime: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "40"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_INSTRUCTIONS_REQUIRED] = "true"

        // Adaugam filtrele DOAR daca au fost selectate (nu sunt goale)
        if (mealType.isNotEmpty()) {
            queries[QUERY_TYPE] = mealType
        }
        if (diet.isNotEmpty()) {
            queries[QUERY_DIET] = diet
        }
        if (maxTime.isNotEmpty()) {
            queries[QUERY_MAX_READY_TIME] = maxTime
        }

        Log.d("RecipesViewModel", "Filters applied: $queries")
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}