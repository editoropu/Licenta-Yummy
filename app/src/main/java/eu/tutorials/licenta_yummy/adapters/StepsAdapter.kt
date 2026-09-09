package eu.tutorials.licenta_yummy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.tutorials.licenta_yummy.R
import eu.tutorials.licenta_yummy.models.instructions_models.Step

class StepsAdapter(private val steps: List<Step>) : RecyclerView.Adapter<StepsAdapter.StepViewHolder>() {

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepNumber: TextView = itemView.findViewById(R.id.step_number)
        val stepInstruction: TextView = itemView.findViewById(R.id.step_instruction)
        val ingredientsList: TextView = itemView.findViewById(R.id.ingredients_list)
        val equipmentList: TextView = itemView.findViewById(R.id.equipment_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instructions, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]

        // Bind data to views
        holder.stepNumber.text = "Step ${step.number}"
        holder.stepInstruction.text = step.step

        // Display ingredients
        holder.ingredientsList.text = "Ingredients: " + step.ingredients.joinToString { it.name }

        // Display equipment
        holder.equipmentList.text = "Equipment: " + step.equipment.joinToString { it.name }
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}
