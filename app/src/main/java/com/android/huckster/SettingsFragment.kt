package com.android.huckster

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.UserData
import com.android.huckster.utils.setNotifCountImage
import com.android.huckster.utils.startAboutHucksterActivity
import com.android.huckster.utils.startDeveloperPageActivity
import com.android.huckster.utils.startProfileActivity

class SettingsFragment : Fragment() {
    private lateinit var imageSwitcher: ImageSwitcher
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupImageSwitcher(view)
        setupUserInfo(view)
        setupMenuItems(view)
        setupLogoutButton(view)

        // Setup notification badge
//        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
//        if (ProductData.getLowStockProductCount() != 0) {
//            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
//        }
    }

    private fun setupImageSwitcher(view: View) {
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
        imageSwitcher.setImageResource(R.drawable.profile_default)
    }

    private fun setupUserInfo(view: View) {
        val textViewName = view.findViewById<TextView>(R.id.name)
        val textViewEmail = view.findViewById<TextView>(R.id.email)

        // Retrieve user data
        val user = UserData.loggedInUser

        if (user != null) {
            textViewName.text = "${user.firstName} ${user.lastName}"
            textViewEmail.text = user.email
        } else {
            sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val firstName = sharedPreferences.getString("firstName", "Unknown")
            val lastName = sharedPreferences.getString("lastName", "User")
            val email = sharedPreferences.getString("email", "No Email")

            textViewName.text = "$firstName $lastName"
            textViewEmail.text = email
        }
    }

    private fun setupMenuItems(view: View) {
        val profileButton = view.findViewById<TextView>(R.id.profile_info)
        profileButton.setOnClickListener {
            startProfileActivity()
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
