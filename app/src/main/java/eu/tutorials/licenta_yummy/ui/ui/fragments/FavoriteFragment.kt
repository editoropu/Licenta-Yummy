package eu.tutorials.licenta_yummy.ui.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import eu.tutorials.licenta_yummy.R
import eu.tutorials.licenta_yummy.adapters.FavoriteAdapter
import eu.tutorials.licenta_yummy.data.DatabaseHelper
import eu.tutorials.licenta_yummy.databinding.FragmentFavoriteRecipesBinding
import eu.tutorials.licenta_yummy.util.Constants.Companion.API_KEY
import eu.tutorials.licenta_yummy.util.NetworkResult
import eu.tutorials.licenta_yummy.util.viewmodels.MainViewModel

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var myDB: DatabaseHelper? = null

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    // Retinem ce ID-uri am cerut ultima data, ca sa nu repetam cererea degeaba
    private var lastLoadedIds: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        myDB = DatabaseHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Un singur observator, inregistrat aici
        mainViewModel.favoriteRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> showShimmerEffect()

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val recipes = response.data
                    if (!recipes.isNullOrEmpty()) {
                        hideMessage()
                        binding.recyclerview.adapter = FavoriteAdapter(ArrayList(recipes))
                    } else {
                        showMessage(
                            "\u2B50",
                            getString(R.string.empty_favorites_title),
                            getString(R.string.empty_favorites_subtitle)
                        )
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    showMessage(
                        "\u26A0\uFE0F",
                        getString(R.string.error_title),
                        response.message ?: getString(R.string.error_generic)
                    )
                }
            }
        }

        binding.emptyStateButton.setOnClickListener {
            lastLoadedIds = null   // fortam reincarcarea
            loadFavorites()
        }
    }

    // Reincarcam de fiecare data cand revenim in tab, ca retetele adaugate
    // sau sterse intre timp sa se vada imediat. Inainte lista se citea o
    // singura data si ramanea invechita.
    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val ids = myDB?.allFavoritesData
            ?.joinToString(separator = ",") { it.recipeId.toString() }
            .orEmpty()

        if (ids.isEmpty()) {
            hideShimmerEffect()
            showMessage(
                "\u2B50",
                getString(R.string.empty_favorites_title),
                getString(R.string.empty_favorites_subtitle)
            )
            lastLoadedIds = ""
            return
        }

        // Aceleasi favorite deja incarcate cu succes -> nu mai consumam o cerere
        if (ids == lastLoadedIds &&
            mainViewModel.favoriteRecipesResponse.value is NetworkResult.Success
        ) {
            return
        }

        lastLoadedIds = ids
        showShimmerEffect()
        mainViewModel.getFavoriteRecipes(ids, API_KEY)
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        showShimmerEffect()
    }

    private fun showMessage(emoji: String, title: String, subtitle: String) {
        _binding?.let {
            it.emptyStateEmoji.text = emoji
            it.emptyStateTitle.text = title
            it.emptyStateSubtitle.text = subtitle
            it.emptyStateLayout.visibility = View.VISIBLE
            it.recyclerview.visibility = View.GONE
        }
    }

    private fun hideMessage() {
        _binding?.let {
            it.emptyStateLayout.visibility = View.GONE
            it.recyclerview.visibility = View.VISIBLE
        }
    }

    private fun showShimmerEffect() {
        _binding?.let {
            it.shimmerLayout1.startShimmer()
            it.shimmerLayout1.visibility = View.VISIBLE
            it.recyclerview.visibility = View.GONE
            it.emptyStateLayout.visibility = View.GONE
        }
    }

    private fun hideShimmerEffect() {
        _binding?.let {
            it.shimmerLayout1.stopShimmer()
            it.shimmerLayout1.visibility = View.GONE
            it.recyclerview.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
