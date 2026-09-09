package eu.tutorials.licenta_yummy.ui.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import eu.tutorials.licenta_yummy.R
import eu.tutorials.licenta_yummy.adapters.RecipesAdapter2
import eu.tutorials.licenta_yummy.databinding.FragmentRecipesBinding
import eu.tutorials.licenta_yummy.util.NetworkResult
import eu.tutorials.licenta_yummy.util.viewmodels.MainViewModel
import eu.tutorials.licenta_yummy.util.viewmodels.RecipesViewModel

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    // Adaptorul se creeaza O SINGURA DATA, gol. Inainte era "lateinit" si se
    // construia abia cand veneau datele, asa ca orice cautare facuta pana atunci
    // arunca UninitializedPropertyAccessException.
    private val mAdapter by lazy { RecipesAdapter2(mutableListOf()) }

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    // Filtrele primite din FilterActivity
    private var mealType = ""
    private var diet = ""
    private var maxTime = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Elemente ramase din versiunea veche, nefolosite acum
        binding.floatingActionButton.visibility = View.GONE
        binding.mealTypeSpinner.visibility = View.GONE

        setupRecyclerView()

        mealType = requireActivity().intent.getStringExtra("MEAL_TYPE") ?: ""
        diet = requireActivity().intent.getStringExtra("DIET") ?: ""
        maxTime = requireActivity().intent.getStringExtra("MAX_TIME") ?: ""

        // Observatorul se inregistreaza O SINGURA DATA, aici.
        // Inainte era inregistrat in interiorul functiei de incarcare, deci se
        // adauga un observator nou la fiecare cerere si raspunsurile se dublau.
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> showShimmerEffect()

                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val results = response.data?.results.orEmpty()
                    if (results.isNotEmpty()) {
                        hideMessage()
                        mAdapter.updateRecipes(results)
                    } else {
                        showMessage(
                            "\uD83C\uDF7D\uFE0F",
                            getString(R.string.empty_recipes_title),
                            getString(R.string.empty_recipes_subtitle)
                        )
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    // Acum aratam motivul real (cheie expirata, fara internet etc.)
                    // in loc sa lasam un ecran alb gol.
                    showMessage(
                        "\u26A0\uFE0F",
                        getString(R.string.error_title),
                        response.message ?: getString(R.string.error_generic)
                    )
                }
            }
        }

        binding.emptyStateButton.setOnClickListener { loadRecipes() }

        // Nu recerem retetele daca le avem deja incarcate. Fiecare cerere consuma
        // din cele ~150 de puncte zilnice ale cheii gratuite, iar revenirea din
        // tab-ul Favorite recrea fragmentul si trimitea inca o cerere degeaba.
        val cached = mainViewModel.recipesResponse.value
        if (cached !is NetworkResult.Success) {
            loadRecipes()
        }
    }

    private fun loadRecipes() {
        showShimmerEffect()
        hideMessage()
        mainViewModel.getRecipes(recipesViewModel.applyFilters(mealType, diet, maxTime))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mAdapter.filterBySearch(query.orEmpty())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filterBySearch(newText.orEmpty())
        return true
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = mAdapter
        showShimmerEffect()
    }

    // Afiseaza cartonasul central cu un mesaj (lista goala sau eroare)
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
