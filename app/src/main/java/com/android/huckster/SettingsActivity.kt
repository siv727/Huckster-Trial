package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.huckster.utils.UserData
import com.android.huckster.utils.shortToast
import com.android.huckster.utils.startAboutHucksterActivity
import com.android.huckster.utils.startDeveloperPageActivity
import com.android.huckster.utils.startProductListActivity
import com.android.huckster.utils.startProfileActivity

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
            startProductListActivity()
        }

        val list_button = findViewById<LinearLayout>(R.id.nav_list)
        list_button.setOnClickListener {
            Log.e("Product List", "Moved to product list!")
            Toast.makeText(this, "Product List", Toast.LENGTH_LONG).show()
            startActivity(Intent(this,ProductListActivity::class.java))
        }

        val profileButton = findViewById<TextView>(R.id.profile_info)
        profileButton.setOnClickListener {
            Log.e("Profile", "Moved to profile!")
            startProfileActivity()
        }

        val aboutHucksterActivity = findViewById<TextView>(R.id.about_app)
        aboutHucksterActivity.setOnClickListener {
            Log.e("About Huclster","Moved to huckster page!")
            startAboutHucksterActivity()
        }
        val aboutDevButton = findViewById<TextView>(R.id.about_dev)
        aboutDevButton.setOnClickListener{
            Log.e("About Developers", "Moved to developer page!")
            startDeveloperPageActivity()
        }

        val logout_button = findViewById<Button>(R.id.button_to_logout)

        logout_button.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }

        btnConfirm.setOnClickListener {
            UserData.loggedInUser = null
            dialog.dismiss()
            this.startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
        dialog.show()
    }
}
