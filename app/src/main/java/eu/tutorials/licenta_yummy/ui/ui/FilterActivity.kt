package eu.tutorials.licenta_yummy.ui.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import eu.tutorials.licenta_yummy.R

class FilterActivity : AppCompatActivity() {

    // Retin ce a selectat utilizatorul in fiecare categorie
    private var selectedMealType: String = ""
    private var selectedDiet: String = ""
    private var selectedTime: String = ""

    // Listele de chip-uri pe categorii (le grupez ca sa pot deselecta restul)
    private lateinit var mealChips: List<TextView>
    private lateinit var dietChips: List<TextView>
    private lateinit var timeChips: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Legam chip-urile de meal type
        val chipBreakfast = findViewById<TextView>(R.id.chipBreakfast)
        val chipLunch = findViewById<TextView>(R.id.chipLunch)
        val chipDinner = findViewById<TextView>(R.id.chipDinner)
        val chipDessert = findViewById<TextView>(R.id.chipDessert)
        mealChips = listOf(chipBreakfast, chipLunch, chipDinner, chipDessert)

        // Legam chip-urile de diet
        val chipAnyDiet = findViewById<TextView>(R.id.chipAnyDiet)
        val chipVegan = findViewById<TextView>(R.id.chipVegan)
        val chipVegetarian = findViewById<TextView>(R.id.chipVegetarian)
        val chipGlutenFree = findViewById<TextView>(R.id.chipGlutenFree)
        dietChips = listOf(chipAnyDiet, chipVegan, chipVegetarian, chipGlutenFree)

        // Legam chip-urile de cooking time
        val chipAnyTime = findViewById<TextView>(R.id.chipAnyTime)
        val chipUnder30 = findViewById<TextView>(R.id.chipUnder30)
        val chip30to60 = findViewById<TextView>(R.id.chip30to60)
        timeChips = listOf(chipAnyTime, chipUnder30, chip30to60)

        val showRecipesButton = findViewById<Button>(R.id.showRecipesButton)
        val browseAllText = findViewById<TextView>(R.id.browseAllText)

        // Selectie implicita, ca ecranul sa nu arate complet gol la deschidere
        selectChip(dietChips, chipAnyDiet)
        selectChip(timeChips, chipAnyTime)

        // --- MEAL TYPE: la apasare, selectez chip-ul si retin valoarea ---
        chipBreakfast.setOnClickListener { selectChip(mealChips, chipBreakfast); selectedMealType = "breakfast" }
        chipLunch.setOnClickListener { selectChip(mealChips, chipLunch); selectedMealType = "main course" }
        chipDinner.setOnClickListener { selectChip(mealChips, chipDinner); selectedMealType = "main course" }
        chipDessert.setOnClickListener { selectChip(mealChips, chipDessert); selectedMealType = "dessert" }

        // --- DIET ---
        chipAnyDiet.setOnClickListener { selectChip(dietChips, chipAnyDiet); selectedDiet = "" }
        chipVegan.setOnClickListener { selectChip(dietChips, chipVegan); selectedDiet = "vegan" }
        chipVegetarian.setOnClickListener { selectChip(dietChips, chipVegetarian); selectedDiet = "vegetarian" }
        chipGlutenFree.setOnClickListener { selectChip(dietChips, chipGlutenFree); selectedDiet = "gluten free" }

        // --- TIME ---
        chipAnyTime.setOnClickListener { selectChip(timeChips, chipAnyTime); selectedTime = "" }
        chipUnder30.setOnClickListener { selectChip(timeChips, chipUnder30); selectedTime = "30" }
        chip30to60.setOnClickListener { selectChip(timeChips, chip30to60); selectedTime = "60" }

        // --- BUTON: trimit filtrele catre ecranul de retete ---
        showRecipesButton.setOnClickListener {
            openRecipes()
        }

        // --- BROWSE ALL: deschid retetele fara filtre ---
        browseAllText.setOnClickListener {
            selectedMealType = ""
            selectedDiet = ""
            selectedTime = ""
            openRecipes()
        }
    }

    // Face chip-ul apasat verde (culoarea temei) si pe restul din grup neutre
    private fun selectChip(group: List<TextView>, selected: TextView) {
        val selectedText = ContextCompat.getColor(this, R.color.md_on_primary)
        val normalText = ContextCompat.getColor(this, R.color.md_on_surface_variant)
        for (chip in group) {
            if (chip == selected) {
                chip.setBackgroundResource(R.drawable.filter_chip_selected)
                chip.setTextColor(selectedText)
            } else {
                chip.setBackgroundResource(R.drawable.filter_chip_unselected)
                chip.setTextColor(normalText)
            }
        }
    }

    // Deschide MainActivity, trimitand filtrele selectate
    private fun openRecipes() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MEAL_TYPE", selectedMealType)
        intent.putExtra("DIET", selectedDiet)
        intent.putExtra("MAX_TIME", selectedTime)
        startActivity(intent)
    }
}