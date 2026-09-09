package eu.tutorials.licenta_yummy.util

class Constants {

    companion object {

        // ================== CHEIA API SPOONACULAR ==================
        // Daca aplicatia nu mai afiseaza retete, aici se schimba cheia.
        // Cont gratuit nou: https://spoonacular.com/food-api/console#Dashboard
        // Planul gratuit are ~150 de puncte pe zi; cand se epuizeaza,
        // serverul raspunde cu 402 si aplicatia nu mai primeste retete.
        //
        // Test rapid: pune cheia in adresa de mai jos si deschide-o in browser.
        // https://api.spoonacular.com/recipes/complexSearch?number=1&apiKey=CHEIA_TA
        const val API_KEY = "38d0bb2a19e247d18fbad67a628b3a60"

        const val BASE_URL = "https://api.spoonacular.com"

        // Chei pentru parametrii cererilor
        const val QUERY_NUMBER = "number"
        const val QUERY_SEARCH = "query"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val MEAL_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_MAX_READY_TIME = "maxReadyTime"
        const val QUERY_INSTRUCTIONS_REQUIRED = "instructionsRequired"
    }
}
