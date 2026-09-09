package eu.tutorials.licenta_yummy.ui.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.tutorials.licenta_yummy.R
import eu.tutorials.licenta_yummy.data.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Legam elementele din layout de cod prin id-urile lor
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val goToRegisterText = findViewById<TextView>(R.id.goToRegisterText)

        // Obiectul care comunica cu baza de date
        val db = DatabaseHelper(this)

        // Ce se intampla cand apesi butonul "Conectare"
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Verificam ca utilizatorul a completat ambele campuri
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completeaza email si parola", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificam in baza de date daca email + parola sunt corecte
            if (db.checkLogin(email, password)) {
                Toast.makeText(this, "Bine ai venit!", Toast.LENGTH_SHORT).show()
                // Deschidem ecranul principal (retetele)
                val intent = Intent(this, FilterActivity::class.java)
                startActivity(intent)
                finish() // inchidem ecranul de login ca sa nu te poti intoarce cu "back"
            } else {
                Toast.makeText(this, "Email sau parola gresita", Toast.LENGTH_SHORT).show()
            }
        }

        // Cand apesi pe "Nu ai cont? Inregistreaza-te" -> mergem la ecranul de inregistrare
        goToRegisterText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}