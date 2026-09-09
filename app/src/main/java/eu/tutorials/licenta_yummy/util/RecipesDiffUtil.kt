package eu.tutorials.licenta_yummy.util

import androidx.recyclerview.widget.DiffUtil
import eu.tutorials.licenta_yummy.models.Result

class RecipesDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare the unique IDs of the items instead of instances
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Use == to check if the content of the two items is the same
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
