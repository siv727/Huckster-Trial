package com.android.huckster

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : Activity() {
    private lateinit var imageSwitcher: ImageSwitcher
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        imageSwitcher = findViewById(R.id.imageSwitcher)

        imageSwitcher.setFactory {
            ImageView(applicationContext).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        imageSwitcher.setImageResource(R.drawable.profile_default)

        val textViewName = findViewById<TextView>(R.id.name)
        val textViewEmail = findViewById<TextView>(R.id.email)

        // Retrieve user data
        val user = UserData.loggedInUser

        if (user != null) {
            // If user is logged in, display details from UserData
            textViewName.text = "${user.firstName} ${user.lastName}"
            textViewEmail.text = user.email
        } else {
            // Fallback to SharedPreferences if UserData is null
            sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val firstName = sharedPreferences.getString("firstName", "Unknown")
            val lastName = sharedPreferences.getString("lastName", "User")
            val email = sharedPreferences.getString("email", "No Email")

            textViewName.text = "$firstName $lastName"
            textViewEmail.text = email
        }

        val homeButton = findViewById<LinearLayout>(R.id.nav_home)
        homeButton.setOnClickListener {
            Log.e("Home", "Moved to home!")
            Toast.makeText(this, "Home Page", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        val profileButton = findViewById<TextView>(R.id.profile_info)
        profileButton.setOnClickListener {
            Log.e("Profile", "Moved to profile!")
            Toast.makeText(this, "Profile Page", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
