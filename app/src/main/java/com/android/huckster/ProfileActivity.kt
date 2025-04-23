package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import com.android.huckster.utils.UserData
import com.android.huckster.utils.shortToast

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
            shortToast("Settings")

            finish()
        }

        val firstNameInput = findViewById<EditText>(R.id.change_firstname)
        val lastNameInput = findViewById<EditText>(R.id.change_lastname)
        val emailInput = findViewById<EditText>(R.id.change_email)
        val saveBtn = findViewById<Button>(R.id.button_save)

        UserData.loggedInUser?.let { user ->
            firstNameInput.setText(user.firstName)
            lastNameInput.setText(user.lastName)
            emailInput.setText(user.email)
        }

        saveBtn.setOnClickListener {
            val newFirstName = firstNameInput.text.toString().trim()
            val newLastName = lastNameInput.text.toString().trim()
            val newEmail = emailInput.text.toString().trim()

            if (newFirstName.isNotEmpty() && newLastName.isNotEmpty() && newEmail.isNotEmpty()) {
                val success = UserData.updateUserProfile(newFirstName, newLastName, newEmail)

                if (success) {
                    shortToast("Profile updated!")
                    finish()
                } else {
                    shortToast( "Failed to update profile!")
                }
            } else {
                shortToast("Please fill out all fields!")
            }
        }
    }
}