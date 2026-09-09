package eu.tutorials.licenta_yummy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eu.tutorials.licenta_yummy.databinding.RecipesRowLayoutBinding
import eu.tutorials.licenta_yummy.models.FoodRecipe
import eu.tutorials.licenta_yummy.models.Result
import eu.tutorials.licenta_yummy.ui.ui.RecipeDetailActivity
import eu.tutorials.licenta_yummy.ui.ui.RecipeStore

class RecipesAdapter2(private var recipes: MutableList<Result>) :
    RecyclerView.Adapter<RecipesAdapter2.MyViewHolder>() {

    private var filteredRecipes: MutableList<Result> = recipes.toMutableList()

    inner class MyViewHolder(binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var deleteImg: ImageView = binding.deleteImageView
        var recipeImageView: ImageView = binding.recipeImageView
        var title: TextView = binding.titleTextView
        var description: TextView = binding.descriptionTextView
        var like: ImageView = binding.heartImageView
        var noOfLikes: TextView = binding.heartTextView
        var clock: ImageView = binding.clockImageView
        var clockTime: TextView = binding.clockTextView
        var leaf: ImageView = binding.leafImageView
        var leafVegan: TextView = binding.leafTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredRecipes.size
    }

    // Updated setData method to handle FoodRecipe and extract Result list
    fun setData(newRecipes: FoodRecipe) {
        recipes.clear()  // Clear the current list
        recipes.addAll(newRecipes.results)  // Add the list of results from FoodRecipe
        filteredRecipes = recipes.toMutableList()  // Update the filtered list
        notifyDataSetChanged()  // Notify the adapter to refresh the RecyclerView
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = filteredRecipes[position]
        Picasso.get().load(recipe.image).into(holder.recipeImageView)

        holder.title.text = recipe.title
        holder.description.text = recipe.summary
        holder.noOfLikes.text = recipe.aggregateLikes.toString()
        holder.clockTime.text = recipe.readyInMinutes.toString()
        holder.leafVegan.text = recipe.vegan.toString()

        holder.itemView.setOnClickListener {
            RecipeStore.recipe = recipe
            val intent = Intent(holder.itemView.context, RecipeDetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteImg.visibility = View.GONE
    }

    // Search function for filtering recipes based on a search query
    fun filterBySearch(query: String) {
        filteredRecipes = if (query.isEmpty()) {
            recipes.toMutableList() // Reset to the full list if the query is empty
        } else {
            recipes.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true)
            }.toMutableList() // Filter by recipe title
        }
        notifyDataSetChanged()
    }

    // Filtering based on selected criteria (checkboxes)
    fun filterRecipes(
        isGlutenFree: Boolean,
        isHealthy: Boolean,
        isVegetarian: Boolean,
        isVegan: Boolean,
        isDairyFree: Boolean,
        isCheap: Boolean
    ) {
        filteredRecipes = recipes.filter { recipe ->
            val matchesGlutenFree = !isGlutenFree || recipe.glutenFree
            val matchesHealthy = !isHealthy || recipe.veryHealthy
            val matchesVegetarian = !isVegetarian || recipe.vegetarian
            val matchesVegan = !isVegan || recipe.vegan
            val matchesDairyFree = !isDairyFree || recipe.dairyFree
            val matchesCheap = !isCheap || recipe.cheap

            matchesGlutenFree && matchesHealthy && matchesVegetarian && matchesVegan && matchesDairyFree && matchesCheap
        }.toMutableList()

        notifyDataSetChanged()
    }

    // Update the list of recipes when new data is loaded
    fun updateRecipes(newRecipes: List<Result>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        filteredRecipes = recipes.toMutableList()
        notifyDataSetChanged()
    }
}
