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

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Legam elementele din layout
        val emailEditText = findViewById<EditText>(R.id.registerEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val goToLoginText = findViewById<TextView>(R.id.goToLoginText)

        val db = DatabaseHelper(this)

        // Cand apesi butonul "Inregistrare"
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Verificam ca toate campurile sunt completate
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Completeaza toate campurile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificam ca email-ul are un format valid
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificam ca parola are minim 6 caractere
            if (password.length < 6) {
                Toast.makeText(this, "Parola trebuie sa aiba minim 6 caractere", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificam ca cele doua parole coincid
            if (password != confirmPassword) {
                Toast.makeText(this, "Parolele nu coincid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificam daca email-ul este deja folosit
            if (db.emailExists(email)) {
                Toast.makeText(this, "Acest email este deja inregistrat", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Totul e ok -> inregistram utilizatorul
            if (db.registerUser(email, password)) {
                Toast.makeText(this, "Cont creat cu succes!", Toast.LENGTH_SHORT).show()
                // Mergem inapoi la ecranul de login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Eroare la inregistrare", Toast.LENGTH_SHORT).show()
            }
        }

        // Cand apesi "Ai deja cont? Conecteaza-te" -> mergem la login
        goToLoginText.setOnClickListener {
            finish() // ne intoarcem la ecranul anterior (login)
        }
    }
}