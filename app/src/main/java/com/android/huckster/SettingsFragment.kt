package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.android.huckster.utils.UserData
import com.android.huckster.utils.startAboutHucksterActivity
import com.android.huckster.utils.startDeveloperPageActivity
import com.android.huckster.utils.startProfileActivity

class SettingsFragment : Fragment() {
    private lateinit var imageSwitcher: ImageSwitcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        loadCachedUserInfo() // Display cached data immediately
        fetchAndDisplayUserInfo() // Fetch fresh data in the background
        setupMenuItems(view)
        setupLogoutButton(view)
    }

    private fun setupViews(view: View) {
        imageSwitcher = view.findViewById(R.id.imageSwitcher)

        imageSwitcher.setFactory {
            ImageView(requireContext()).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        // Load saved image from SharedPreferences
        val savedImage = UserData.loadProfileImage(requireContext())
        if (savedImage != null) {
            Glide.with(this)
                .load(savedImage)
                .placeholder(R.drawable.profile_default)
                .into(imageSwitcher.currentView as ImageView)
        } else {
            Glide.with(this)
                .load(R.drawable.profile_default)
                .into(imageSwitcher.currentView as ImageView)
        }
    }

    fun fetchAndDisplayUserInfo() {
        val textViewName = view?.findViewById<TextView>(R.id.name)
        val textViewEmail = view?.findViewById<TextView>(R.id.email)

        // Fetch user data from Firebase asynchronously
        UserData.fetchLoggedInUser { user ->
            if (user != null) {
                // Update UI with fresh data
                textViewName?.text = "${user.firstName} ${user.lastName}"
                textViewEmail?.text = user.email

                // Cache the user data
                val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("firstName", user.firstName)
                    .putString("lastName", user.lastName)
                    .putString("email", user.email)
                    .apply()

                // Load profile image from Firebase
                val savedImage = UserData.loadProfileImage(requireContext())
                if (savedImage != null) {
                    Glide.with(this)
                        .load(savedImage)
                        .placeholder(R.drawable.profile_default)
                        .into(imageSwitcher.currentView as ImageView)
                }
            } else {
                // Handle error case
                textViewName?.text = "Unknown User"
                textViewEmail?.text = "No Email"
                Glide.with(this)
                    .load(R.drawable.profile_default)
                    .into(imageSwitcher.currentView as ImageView)
            }
        }
    }

    private fun setupMenuItems(view: View) {
        val profileButton = view.findViewById<TextView>(R.id.profile_info)
        profileButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE)
        }

        val aboutHucksterActivity = view.findViewById<TextView>(R.id.about_app)
        aboutHucksterActivity.setOnClickListener {
            startAboutHucksterActivity()
        }

        val aboutDevButton = view.findViewById<TextView>(R.id.about_dev)
        aboutDevButton.setOnClickListener {
            startDeveloperPageActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PROFILE_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Refresh user details
            fetchAndDisplayUserInfo()

            // Load saved image from SharedPreferences
            val savedImage = UserData.loadProfileImage(requireContext())
            if (savedImage != null) {
                Glide.with(this)
                    .load(savedImage)
                    .placeholder(R.drawable.profile_default)
                    .into(imageSwitcher.currentView as ImageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.profile_default)
                    .into(imageSwitcher.currentView as ImageView)
            }
        }
    }

    companion object {
        private const val PROFILE_UPDATE_REQUEST_CODE = 1
    }

    private fun loadCachedUserInfo() {
        val textViewName = view?.findViewById<TextView>(R.id.name)
        val textViewEmail = view?.findViewById<TextView>(R.id.email)

        // Load cached user info
        val cachedUser = UserData.loadUserData(requireContext())
        if (cachedUser != null) {
            textViewName?.text = "${cachedUser.firstName} ${cachedUser.lastName}"
            textViewEmail?.text = cachedUser.email

            // Load profile image from cached photo
            Glide.with(this)
                .load(cachedUser.photo) // Can be Base64 decoded or a URL
                .placeholder(R.drawable.profile_default)
                .into(imageSwitcher.currentView as ImageView)
        } else {
            // Display default values
            textViewName?.text = "Unknown User"
            textViewEmail?.text = "No Email"
            Glide.with(this)
                .load(R.drawable.profile_default)
                .into(imageSwitcher.currentView as ImageView)
        }
    }

    private fun setupLogoutButton(view: View) {
        val logoutButton = view.findViewById<Button>(R.id.button_to_logout)
        logoutButton.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            UserData.loggedInUser = null
            dialog.dismiss()
            requireActivity().startActivity(
                Intent(requireContext(), LoginActivity::class.java)
            )
            requireActivity().finishAffinity()
        }
        dialog.show()
    }
}