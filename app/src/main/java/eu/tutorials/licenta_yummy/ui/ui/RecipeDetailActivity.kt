package eu.tutorials.licenta_yummy.ui.ui

import Instructions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eu.tutorials.licenta_yummy.R
import eu.tutorials.licenta_yummy.ui.ui.fragments.IngredientsFragment
import eu.tutorials.licenta_yummy.ui.ui.fragments.OverviewFragment

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {

    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android poate reporni aplicatia direct pe acest ecran, dupa ce a fost
        // inchisa in fundal. Atunci reteta nu mai e in memorie si vechiul cod
        // arunca UninitializedPropertyAccessException. Acum ne intoarcem elegant.
        if (!RecipeStore.isReady) {
            finish()
            return
        }

        setContentView(R.layout.activity_recipe_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Titlul retetei in bara de sus + sageata de intoarcere
        supportActionBar?.title = RecipeStore.recipe.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabs)

        // Set the ViewPager adapter
        viewPager!!.setAdapter(FragmentAdapter(this))

        // Attach the TabLayout with ViewPager
        TabLayoutMediator(tabLayout!!, viewPager!!,
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
//                val tabTextView = LayoutInflater.from(this@RecipeDetailActivity).inflate(R.layout.custom_tab, null) as TextView
                when (position) {
                    0 -> tab.setText(R.string.tab_overview)
                    1 -> tab.setText(R.string.tab_ingredients)
                    2 -> tab.setText(R.string.tab_instructions)
                }
//                tabTextView.textSize = 16f // Set your desired text size
//                tab.customView = tabTextView
            }).attach()
    }

    // Adapter class for ViewPager2
    private class FragmentAdapter(fragmentActivity: AppCompatActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OverviewFragment() // Your fragment class for Overview
                1 -> IngredientsFragment() // Your fragment class for Ingredients
                2 -> Instructions() // Your fragment class for Instructions
                else -> OverviewFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3 // Number of tabs
        }
    }
}
