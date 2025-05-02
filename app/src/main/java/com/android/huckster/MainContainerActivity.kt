package com.android.huckster

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.huckster.utils.NotificationHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.UserData
import com.android.huckster.utils.refreshNotificationBadge

class MainContainerActivity : AppCompatActivity() {
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)


        UserData.preloadUserData(this)
        ProductData.preloadProducts { success ->
            if (!success) {
                Toast.makeText(this, "Failed to load products.", Toast.LENGTH_SHORT).show()
            }

        }

        refreshNotificationBadge()

        // Retrieve NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)

        // Reference to notification count image
        val notifCountImageView = findViewById<ImageView>(R.id.notification_badge)
        // Fetch threshold from SharedPreferences
        val sharedPref = getSharedPreferences("StockPrefs", MODE_PRIVATE)
        val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)

        // Fetch low-stock count and update badge image
        ProductData.getLowStockNotificationCount(lowStockThreshold) { count ->
            runOnUiThread {
                val imageName = when {
                    count <= 0 -> null // No notification
                    count in 1..9 -> "notif_$count"
                    else -> "notif_10"
                }

                if (imageName != null) {
                    val resId = resources.getIdentifier(imageName, "drawable", packageName)
                    notifCountImageView.setImageResource(resId)
                    notifCountImageView.visibility = View.VISIBLE
                } else {
                    notifCountImageView.visibility = View.GONE
                }
            }
        }



        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.navigation_products -> {
                    navController.navigate(R.id.productListFragment)
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.notificationsFragment)
                    true
                }
                R.id.navigation_account -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list when the fragment resumes

        updateNotificationBadge()
    }
    fun updateNotificationBadge() {
        val sharedPref = getSharedPreferences("StockPrefs", MODE_PRIVATE)
        val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)

        ProductData.getLowStockNotificationCount(lowStockThreshold) { count ->
            runOnUiThread {
                val imageName = when {
                    count <= 0 -> null
                    count in 1..9 -> "notif_$count"
                    else -> "notif_10"
                }

                val notifCountImageView = findViewById<ImageView>(R.id.notification_badge)
                if (imageName != null) {
                    val resId = resources.getIdentifier(imageName, "drawable", packageName)
                    notifCountImageView.setImageResource(resId)
                    notifCountImageView.visibility = View.VISIBLE
                } else {
                    notifCountImageView.visibility = View.GONE
                }
            }
        }
    }




}
