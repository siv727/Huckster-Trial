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

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edittext_email = findViewById<EditText>(R.id.edittext_email)
        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val textViewRegister: TextView = findViewById(R.id.textview_register)



        textViewRegister.text = " Register!"

        // Apply gradient text color
        val paint: TextPaint = textViewRegister.paint
        val width: Float = paint.measureText(textViewRegister.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, textViewRegister.textSize,
            intArrayOf(
                Color.parseColor("#FFA500"),
                Color.parseColor("#FFA500"),
                Color.parseColor("#C24733"),

                ),
            null, Shader.TileMode.CLAMP
        )
        textViewRegister.paint.shader = textShader

        var first_name = "";
        var last_name = "";
        intent?.let{
            it.getStringExtra("email")?.let{ email ->
                edittext_email.setText(email)
            }

            it.getStringExtra("password")?.let{ password ->
                edittext_password.setText(password)
            }

            it.getStringExtra("fname")?.let{ fname ->
                first_name = fname
            }

            it.getStringExtra("lname")?.let{ lname ->
                last_name = lname
            }
        }

        val text_register = findViewById<LinearLayout>(R.id.layout_register)
        text_register.setOnClickListener {
            Log.e("Register", "Moved to Register Page")
            Toast.makeText(this, "Make an account!", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        val button_login = findViewById<Button>(R.id.button_login)
        button_login.setOnClickListener {
            Log.e("Log In", "Logging in!")

            if(edittext_email.text.isNullOrEmpty() || edittext_password.text.isNullOrEmpty()) {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // put another condition for both email (proper email format) and password (no slashes, dashes, underscores, punctuation marks)
            // validate email and password accdg to set email and pass -sir mobdev
            // can be from server data or static -sir mobdev

            Log.e("Log In", "Successful Log in!")
            Toast.makeText(this, "Welcome to Huckster!", Toast.LENGTH_LONG).show()
            startActivity(
                Intent(this, HomeActivity::class.java).apply{
                    putExtra("email", edittext_email.text.toString())
                    putExtra("password", edittext_password.text.toString())
                    putExtra("fname", first_name)
                    putExtra("lname", last_name)
                }
            )
        }


    }
}