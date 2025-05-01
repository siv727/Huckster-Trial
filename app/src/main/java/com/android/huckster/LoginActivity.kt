package com.android.huckster

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.huckster.utils.UserData
import com.android.huckster.utils.longToast
import com.android.huckster.utils.startMainContainerActivity
import com.android.huckster.utils.startRegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edittextEmail = findViewById<EditText>(R.id.edittext_email)
        val edittextPassword = findViewById<EditText>(R.id.edittext_password)
        val checkBoxRememberMe = findViewById<CheckBox>(R.id.checkbox_remember_me)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val textViewRegister: TextView = findViewById(R.id.textview_register)

        // Load saved credentials (if any)
        val preferences = getSharedPreferences("HucksterPrefs", Context.MODE_PRIVATE)
        val savedEmail = preferences.getString("email", "")
        val savedPassword = preferences.getString("password", "")
        val rememberMe = preferences.getBoolean("rememberMe", false)

        if (rememberMe) {
            edittextEmail.setText(savedEmail)
            edittextPassword.setText(savedPassword)
            checkBoxRememberMe.isChecked = true
        }

        // Apply gradient text color to Register link
        applyGradientText(textViewRegister)

        // Navigate to RegisterActivity
        findViewById<LinearLayout>(R.id.layout_register).setOnClickListener {
            startRegisterActivity()
        }

        // Handle intent data (email and password from RegisterActivity)
        intent?.let {
            it.getStringExtra("email")?.let { email -> edittextEmail.setText(email) }
            it.getStringExtra("password")?.let { password -> edittextPassword.setText(password) }
        }

        // Login button click listener
        buttonLogin.setOnClickListener {
            val email = edittextEmail.text.toString().trim()
            val password = edittextPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                longToast("Email and Password cannot be empty")
                return@setOnClickListener
            }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        if (checkBoxRememberMe.isChecked) {
                            saveLoginCredentials(email, password)
                        } else {
                            clearLoginCredentials()
                        }

                        longToast("Welcome to Huckster!")
                        onLoginSuccess()
                    } else {
                        longToast("Sign-In Failed: ${task.exception?.message}")
                    }
                }
        }
    }

    private fun onLoginSuccess() {
        startMainContainerActivity() // Instead of startHomeActivity()
        finish()
    }

    private fun applyGradientText(textView: TextView) {
        textView.text = " Register!"
        val paint: TextPaint = textView.paint
        val width: Float = paint.measureText(textView.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(Color.parseColor("#FFA500"), Color.parseColor("#C24733")),
            null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }

    private fun saveLoginCredentials(email: String, password: String) {
        val preferences = getSharedPreferences("HucksterPrefs", Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString("email", email)
            putString("password", password)
            putBoolean("rememberMe", true)
            apply()
        }
    }

    private fun clearLoginCredentials() {
        val preferences = getSharedPreferences("HucksterPrefs", Context.MODE_PRIVATE)
        with(preferences.edit()) {
            remove("email")
            remove("password")
            putBoolean("rememberMe", false)
            apply()
        }
    }

}
