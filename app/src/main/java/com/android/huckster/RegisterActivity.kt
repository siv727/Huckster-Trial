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
            Toast.makeText(this, "Sign in!", Toast.LENGTH_LONG).show()

            val login_intent = Intent(this, LoginActivity::class.java)
            startActivity(login_intent)
        }

        val button_landing = findViewById<Button>(R.id.button_to_login)
        button_landing.setOnClickListener {
            Log.e("Register", "Logging in credentials!")

            val email = edittext_email.text
            val password = edittext_password.text
            val fname = edittext_fname.text
            val lname = edittext_lname.text
            val checkpass = edittext_checkpass.text

            if(email.isNullOrEmpty() || password.isNullOrEmpty() || fname.isNullOrEmpty() || lname.isNullOrEmpty() || checkpass.isNullOrEmpty()) {
                Toast.makeText(this, "Fill out everything with your details to sign up!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // put another condition for both email (proper email format) and password (no slashes, dashes, underscores, punctuation marks)

            Log.e("Log in", "Successful Registration!")
            Toast.makeText(this, "Log in!", Toast.LENGTH_LONG).show()

            val login_intent = Intent(this, LoginActivity::class.java)
            startActivity(login_intent)
        }
    }
}