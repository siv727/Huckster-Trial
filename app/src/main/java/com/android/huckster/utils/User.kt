package com.android.huckster.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.android.huckster.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val photo: String = "" // URL for the user's profile photo
)

object UserData {
    var loggedInUser: User? = null // Stores currently logged-in user

    // Save profile image to SharedPreferences as Base64
    fun saveProfileImage(context: Context, bitmap: Bitmap?) {
        bitmap?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val baos = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val imageBytes = baos.toByteArray()
                val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("profile_image", imageString).apply()
            }
        }
    }

    // Load profile image from SharedPreferences
    fun loadProfileImage(context: Context): Bitmap? {
        val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        val imageString = sharedPreferences.getString("profile_image", null)
        return if (imageString != null) {
            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } else {
            null
        }
    }

    // Register a new user in Firebase Realtime Database
    fun registerUser(
        context: Context,
        firstName: String,
        lastName: String,
        email: String,
        callback: (Boolean) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            callback(false) // User not authenticated
            return
        }

        // Default photo as a drawable resource
        val defaultPhotoUrl = "android.resource://${context.packageName}/${R.drawable.profile_default}"

        val user = User(firstName, lastName, email, defaultPhotoUrl)

        // Perform database operation in the background
        FirebaseDatabase.getInstance().getReference("Users").child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Clear any existing cached profile image
                    context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
                        .edit().clear().apply()
                }
                callback(task.isSuccessful)
            }
    }

    // Fetch logged-in user details
    fun fetchLoggedInUser(callback: (User?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            callback(null)
            return
        }

        FirebaseDatabase.getInstance().getReference("Users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                loggedInUser = user
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // Update user profile
    fun updateUserProfile(
        context: Context,
        firstName: String,
        lastName: String,
        callback: (Boolean) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            callback(false)
            return
        }

        // Retrieve photo from SharedPreferences in the background
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
            val photoBase64 = sharedPreferences.getString("profile_image", null)

            // Prepare updates map
            val updates = mutableMapOf<String, Any>(
                "firstName" to firstName,
                "lastName" to lastName
            )
            if (photoBase64 != null) {
                updates["photo"] = photoBase64 // Include photo if it exists
            }

            // Update Firebase Realtime Database
            FirebaseDatabase.getInstance().getReference("Users").child(userId).updateChildren(updates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update the locally cached user object
                        loggedInUser = loggedInUser?.copy(
                            firstName = firstName,
                            lastName = lastName,
                            photo = photoBase64 ?: loggedInUser?.photo ?: ""
                        )
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
        }
    }

    fun saveUserData(context: Context, user: User) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("firstName", user.firstName)
            .putString("lastName", user.lastName)
            .putString("email", user.email)
            .putString("photo", user.photo) // Save photo as Base64 or URL
            .apply()
    }

    fun loadUserData(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", null)
        val lastName = sharedPreferences.getString("lastName", null)
        val email = sharedPreferences.getString("email", null)
        val photo = sharedPreferences.getString("photo", null)

        return if (firstName != null && lastName != null && email != null) {
            User(firstName, lastName, email, photo ?: "")
        } else {
            null
        }
    }

    fun clearCachedUserData(context: Context) {
//        val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
//        sharedPreferences.edit().clear().apply()

        val userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userPrefs.edit().clear().apply()
    }

    fun preloadUserData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val firstName = sharedPreferences.getString("firstName", null)
            val lastName = sharedPreferences.getString("lastName", null)
            val email = sharedPreferences.getString("email", null)
            val photo = sharedPreferences.getString("photo", null)

            if (firstName != null && lastName != null && email != null) {
                loggedInUser = User(firstName, lastName, email, photo ?: "")
            }
        }
    }
}