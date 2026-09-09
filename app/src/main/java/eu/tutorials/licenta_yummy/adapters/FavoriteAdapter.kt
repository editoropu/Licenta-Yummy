package eu.tutorials.licenta_yummy.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eu.tutorials.licenta_yummy.data.DatabaseHelper
import eu.tutorials.licenta_yummy.databinding.RecipesRowLayoutBinding
import eu.tutorials.licenta_yummy.models.Result
import eu.tutorials.licenta_yummy.ui.ui.RecipeStore
import eu.tutorials.licenta_yummy.ui.ui.RecipeDetailActivity

class FavoriteAdapter(val recipes: ArrayList<Result>) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

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
        return recipes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipes[position]
        Picasso.get().load(recipe.image).into(holder.recipeImageView)

        Log.d("recipe.image",recipe.image);
        holder.title.text = recipe.title
        holder.description.text = recipe.summary
        holder.noOfLikes.text = recipe.aggregateLikes.toString()
        holder.clockTime.text = recipe.readyInMinutes.toString()
        holder.leafVegan.text = recipe.vegan.toString()


        holder.itemView.setOnClickListener(View.OnClickListener {
            RecipeStore.recipe = recipe
            val intent = Intent(holder.itemView.context, RecipeDetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        })
        holder.deleteImg.setOnClickListener(View.OnClickListener {
            RecipeStore.recipe = recipe
            val myDB = DatabaseHelper(holder.itemView.context)
            myDB.deleteFavorite(recipe.id)
            Toast.makeText(holder.itemView.context,"Removed from favorites",Toast.LENGTH_SHORT).show()
            recipes.removeAt(position)
            notifyDataSetChanged()
        })
    }
}
