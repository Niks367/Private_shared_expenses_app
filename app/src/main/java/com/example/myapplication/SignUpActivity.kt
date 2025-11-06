package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Profile
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupScreenBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)

        binding.btnSignUp.setOnClickListener {
            val name = binding.nameEdit.text?.toString()?.trim().orEmpty()
            val email = binding.emailEdit.text?.toString()?.trim().orEmpty()
            val phone = binding.phoneEdit.text?.toString()?.trim()
            val password = binding.passwordEdit.text?.toString().orEmpty()

            // ----- Basic validation -----
            when {
                name.isEmpty() -> binding.nameLayout.error = "Enter a name"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    binding.emailLayout.error = "Invalid email"
                password.length < 6 -> binding.passwordLayout.error = "≥6 characters"
                else -> {
                    // Clear errors
                    binding.nameLayout.error = null
                    binding.emailLayout.error = null
                    binding.passwordLayout.error = null

                    // Hash password and store profile
                    val hashed = hashPassword(password)
                    val profile =
                        Profile(name = name, email = email, phone = phone, passwordHash = hashed)

                    lifecycleScope.launch {
                        db.profileDao().insert(profile)
                        Toast.makeText(this@SignUpActivity, "Account created!", Toast.LENGTH_SHORT).show()
                        // TODO: navigate to main screen or login flow
                    }
                }
            }
        }
    }
}