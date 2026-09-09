import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import eu.tutorials.licenta_yummy.adapters.StepsAdapter
import eu.tutorials.licenta_yummy.databinding.FragmentInstrcutionsBinding
import eu.tutorials.licenta_yummy.models.instructions_models.Instruction
import eu.tutorials.licenta_yummy.ui.ui.RecipeStore
import eu.tutorials.licenta_yummy.ui.ui.SpoonacularApiClient

class Instructions : Fragment() {
    private var binding: FragmentInstrcutionsBinding? = null
    private var apiClient: SpoonacularApiClient? = null
    private lateinit var stepsAdapter: StepsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInstrcutionsBinding.inflate(inflater, container, false)
        val view: View = binding!!.root

        apiClient = SpoonacularApiClient()
        b.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding!!.shimmerLayout1.visibility = View.VISIBLE

        // Cererea de retea ruleaza pe un fir separat
        Thread {
            try {
                val instructions = apiClient!!.getAnalyzedInstructions(RecipeStore.recipe.id)
                Log.d("YummyAPI", "Instructiuni: $instructions")

                activity?.runOnUiThread { parseResponse(instructions) }
            } catch (e: Exception) {
                // Orice esec (fara internet, cheie expirata, JSON invalid) trebuie
                // sa arate mesajul din ecran, nu sa lase un shimmer care se invarte la infinit.
                Log.e("YummyAPI", "Instructiunile nu au putut fi incarcate", e)
                activity?.runOnUiThread { showNoInstructions() }
            }
        }.start()

        return view
    }

    private fun showNoInstructions() {
        binding?.let {
            it.recyclerview.visibility = View.GONE
            it.errorTextView.visibility = View.VISIBLE
            it.shimmerLayout1.visibility = View.GONE
        }
    }

    private fun parseResponse(instructions: String) {
        val b = binding ?: return

        val instructionsList: List<Instruction> = try {
            Gson().fromJson(instructions, Array<Instruction>::class.java)?.toList().orEmpty()
        } catch (e: Exception) {
            Log.e("YummyAPI", "JSON invalid pentru instructiuni", e)
            emptyList()
        }

        if (instructionsList.isNotEmpty() && instructionsList[0].steps.isNotEmpty()) {
            // Reteta ARE instructiuni -> afisam pasii, ascundem mesajul
            val steps = instructionsList[0].steps
            stepsAdapter = StepsAdapter(steps)
            b.recyclerview.adapter = stepsAdapter
            b.recyclerview.visibility = View.VISIBLE
            b.errorTextView.visibility = View.INVISIBLE
        } else {
            // Reteta NU are instructiuni -> afisam mesajul frumos, ascundem lista
            b.recyclerview.visibility = View.GONE
            b.errorTextView.visibility = View.VISIBLE
        }
        b.shimmerLayout1.visibility = View.GONE
    }
}