package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.huckster.utils.UserData
import com.android.huckster.utils.longToast
import com.android.huckster.utils.startLoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edittext_email = findViewById<EditText>(R.id.edittext_email)
        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val edittext_fname = findViewById<EditText>(R.id.edittext_firstname)
        val edittext_lname = findViewById<EditText>(R.id.edittext_lastname)
        val edittext_checkpass = findViewById<EditText>(R.id.edittext_confirm_password)
        val textViewLogin: TextView = findViewById(R.id.textview_login)

        textViewLogin.text = " Login!"

        // Apply gradient text color
        val paint: TextPaint = textViewLogin.paint
        val width: Float = paint.measureText(textViewLogin.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, textViewLogin.textSize,
            intArrayOf(
                Color.parseColor("#FFA500"),
                Color.parseColor("#FFA500"),
                Color.parseColor("#C24733"),
            ),
            null, Shader.TileMode.CLAMP
        )
        textViewLogin.paint.shader = textShader

        val text_login = findViewById<LinearLayout>(R.id.layout_login)
        text_login.setOnClickListener {
            Log.e("Log in", "Moved to Login Page")
            longToast("Sign in!")
            startLoginActivity()
        }

        val button_landing = findViewById<Button>(R.id.button_to_login)
        button_landing.setOnClickListener {
            Log.e("Register", "Logging in credentials!")

            val email = edittext_email.text.toString().trim()
            val password = edittext_password.text.toString().trim()
            val firstName = edittext_fname.text.toString().trim()
            val lastName = edittext_lname.text.toString().trim()
            val checkpass = edittext_checkpass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || checkpass.isEmpty()) {
                longToast("Fill out everything with your details to sign up!")
                return@setOnClickListener
            }

            if (password != checkpass) {
                longToast("Password Mismatch!")
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Save user details to the Realtime Database
                        UserData.registerUser(this, firstName, lastName, email) { success ->
                            if (success) {
                                Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                                finish() // Close RegisterActivity and return to LoginActivity
                            } else {
                                Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Sign-Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            startActivity(
                Intent(this, LoginActivity::class.java).apply{
                    putExtra("email", email)
                    putExtra("password", password)
                }
            )
        }
    }
}