package com.android.huckster

import android.app.Activity
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        imageSwitcher = findViewById(R.id.imageSwitcher) // Initialized inside onCreate()

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

        val textview_name = findViewById<TextView>(R.id.name)
        val textview_email = findViewById<TextView>(R.id.email)

        intent?.let{
            it.getStringExtra("fname")?.let{ fname->
                textview_name.setText("$fname ")
            }

            it.getStringExtra("lname")?.let{ lname->
                textview_name.append("$lname")
            }

            it.getStringExtra("email")?.let{ email ->
                textview_email.setText("$email")
            }
        }

        val home_button = findViewById<LinearLayout>(R.id.nav_home)
        home_button.setOnClickListener {
            Log.e("Home", "Moved to home!")
            Toast.makeText(this, "Home Page", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, HomeActivity::class.java)
            )
        }

        val profile_button = findViewById<TextView>(R.id.profile_info)
        profile_button.setOnClickListener {
            Log.e("Profile", "Moved to profile!")
            Toast.makeText(this, "Profile Page", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
        }

        /*      Developer Page
                val dev_button = findViewById<TextView>(R.id.about_dev)
                dev_button.setOnClickListener {
                    Log.e("Developer", "Moved to developer info!")
                    Toast.makeText(this, "About us", Toast.LENGTH_LONG).show()

                    val dev_intent = Intent(this, DeveloperActivity::class.java)
                    startActivity(dev_intent)
                }
        */
    }
}