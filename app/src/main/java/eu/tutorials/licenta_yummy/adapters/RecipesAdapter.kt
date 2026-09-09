//package eu.tutorials.licenta_yummy.adapters
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import eu.tutorials.licenta_yummy.databinding.RecipesRowLayoutBinding
//import eu.tutorials.licenta_yummy.models.FoodRecipe
//import eu.tutorials.licenta_yummy.models.Result
//import eu.tutorials.licenta_yummy.util.RecipesDiffUtil
//
//class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
//
//    private var recipes = emptyList<Result>()
//
//    class MyViewHolder(private val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(result: Result) {
//            Log.d("API Adapter", "Binding data for: ${result.title}")
//            binding.result = result
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): MyViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
//                return MyViewHolder(binding)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder.from(parent)
//    }
//
//    override fun getItemCount(): Int {
//        Log.d("API Adapter data", "getItemCount recipes ${recipes.size}")
//        return recipes.size
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentRecipe = recipes[position]
//        Log.d("API Adapter", "onBindViewHolder currentRecipe ${currentRecipe.title}")
//        holder.bind(currentRecipe)
//    }
//
//    fun setData(newData: FoodRecipe) {
//        Log.d("API Adapter data", "setData recipes ${recipes.size}")
//        Log.d("API Adapter data", "setData newData ${newData.results[0].title}")
//        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
//        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
//        recipes = newData.results
//        diffUtilResult.dispatchUpdatesTo(this)
//
//    }
//}
