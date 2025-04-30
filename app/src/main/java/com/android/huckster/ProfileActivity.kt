package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.android.huckster.utils.UserData
import com.android.huckster.utils.shortToast
import java.io.InputStream

class ProfileActivity : Activity() {
    private lateinit var changeImageSwitcher: ImageSwitcher
    private var selectedImageUri: Uri? = null // To hold the selected image URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        changeImageSwitcher = findViewById(R.id.change_pic)

        changeImageSwitcher.setFactory {
            ImageView(applicationContext).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        // Load saved image from SharedPreferences
        val savedImage = UserData.loadProfileImage(this)
        if (savedImage != null) {
            Glide.with(this)
                .load(savedImage)
                .placeholder(R.drawable.profile_default)
                .into(changeImageSwitcher.currentView as ImageView)
        } else {
            Glide.with(this)
                .load(R.drawable.profile_default)
                .into(changeImageSwitcher.currentView as ImageView)
        }

        // Handle image selection on click
        changeImageSwitcher.setOnClickListener {
            openGallery()
        }

        val settingsButton = findViewById<ImageView>(R.id.back_settings)
        settingsButton.setOnClickListener {
            finish()
        }

        val firstNameInput = findViewById<EditText>(R.id.change_firstname)
        val lastNameInput = findViewById<EditText>(R.id.change_lastname)
        val saveBtn = findViewById<Button>(R.id.button_save)

        // Load user information
        UserData.loggedInUser?.let { user ->
            firstNameInput.setText(user.firstName)
            lastNameInput.setText(user.lastName)
        }

        saveBtn.setOnClickListener {
            val newFirstName = firstNameInput.text.toString().trim()
            val newLastName = lastNameInput.text.toString().trim()

            if (newFirstName.isNotEmpty() && newLastName.isNotEmpty()) {
                UserData.updateUserProfile(
                    this,
                    newFirstName,
                    newLastName
                ) { success ->
                    if (success) {
                        shortToast("Profile updated!")
                        setResult(Activity.RESULT_OK) // Notify that profile was updated
                        finish()
                    } else {
                        shortToast("Failed to update profile!")
                    }
                }
            } else {
                shortToast("Please fill out all fields!")
            }
        }
    }

    // Open gallery to pick an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                val bitmap = uriToBitmap(uri)
                if (bitmap != null) {
                    UserData.saveProfileImage(this, bitmap) // Save image to shared preferences
                    Glide.with(this)
                        .load(bitmap)
                        .placeholder(R.drawable.profile_default)
                        .into(changeImageSwitcher.currentView as ImageView)
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("image_updated", true)
                    })
                } else {
                    shortToast("Failed to load image!")
                }
            } ?: shortToast("No image selected!")
        }
    }

    // Convert Uri to Bitmap
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
    }
}