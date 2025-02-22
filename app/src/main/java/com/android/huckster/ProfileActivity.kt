package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast

class ProfileActivity : Activity() {
    private lateinit var changeImageSwitcher: ImageSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        changeImageSwitcher = findViewById(R.id.change_pic) // Initialized inside onCreate()

        changeImageSwitcher.setFactory {
            ImageView(applicationContext).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        changeImageSwitcher.setImageResource(R.drawable.profile_default)

        val settings_button = findViewById<ImageView>(R.id.back_settings)

        settings_button.setOnClickListener {
            Log.e("Settings", "Back to settings")
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()

            val settings_intent = Intent(this, SettingsActivity::class.java)
            startActivity(settings_intent)
        }
    }
}