package eu.tutorials.licenta_yummy.ui.ui

import eu.tutorials.licenta_yummy.models.Result

object RecipeStore {

    lateinit var recipe: Result

    /**
     * Android poate inchide aplicatia in fundal si sa o redeschida direct pe
     * ecranul de detaliu. In acel moment "recipe" nu mai e setat, iar accesarea
     * lui arunca UninitializedPropertyAccessException. Ecranele verifica intai
     * acest flag.
     */
    val isReady: Boolean
        get() = ::recipe.isInitialized
}
