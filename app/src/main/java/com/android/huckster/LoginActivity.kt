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
import com.android.huckster.utils.UserData
import com.android.huckster.utils.longToast
import com.android.huckster.utils.startHomeActivity
import com.android.huckster.utils.startRegisterActivity

class LoginActivity : Activity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edittextEmail = findViewById<EditText>(R.id.edittext_email)
        val edittextPassword = findViewById<EditText>(R.id.edittext_password)
        val checkBoxRememberMe = findViewById<CheckBox>(R.id.checkbox_remember_me)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val textViewRegister: TextView = findViewById(R.id.textview_register)

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        // Load saved credentials
        loadPreferences(edittextEmail, edittextPassword, checkBoxRememberMe)

        // Apply gradient text color to Register link
        textViewRegister.text = " Register!"
        val paint: TextPaint = textViewRegister.paint
        val width: Float = paint.measureText(textViewRegister.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textViewRegister.textSize,
            intArrayOf(Color.parseColor("#FFA500"), Color.parseColor("#C24733")),
            null, Shader.TileMode.CLAMP
        )
        textViewRegister.paint.shader = textShader

        // Navigate to RegisterActivity
        findViewById<LinearLayout>(R.id.layout_register).setOnClickListener {
            startRegisterActivity()
        }

        // Login button click listener
        buttonLogin.setOnClickListener {
            val email = edittextEmail.text.toString().trim()
            val password = edittextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                longToast("Email and Password cannot be empty")
                return@setOnClickListener
            }

            // Authenticate user using UserData
            if (UserData.authenticate(email, password)) {
                longToast("Welcome to Huckster!")

                // Store login details locally if "Remember Me" is checked
                if (checkBoxRememberMe.isChecked) {
                    savePreferences(email, password)
                } else {
                    clearPreferences()
                }

                startHomeActivity()
            } else {
                longToast("Invalid email or password!")
            }
        }
    }

    private fun savePreferences(email: String, password: String) {
        val user = UserData.loggedInUser // Get logged-in user
        if (user != null) {
            val editor = sharedPreferences.edit()
            editor.putString("email", user.email)
            editor.putString("firstName", user.firstName)
            editor.putString("lastName", user.lastName)
            editor.putString("password", password) // Store password if needed
            editor.putBoolean("rememberMe", true)
            editor.apply()
        }
    }


    private fun loadPreferences(edittextEmail: EditText, edittextPassword: EditText, checkBoxRememberMe: CheckBox) {
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")
        val isRemembered = sharedPreferences.getBoolean("rememberMe", false)

        if (isRemembered) {
            edittextEmail.setText(savedEmail)
            edittextPassword.setText(savedPassword)
            checkBoxRememberMe.isChecked = true
        }
    }

    private fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
