package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.UserData

class MainContainerActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        UserData.preloadUserData(this)
        ProductData.preloadProducts { success ->
            if (!success) {
                Toast.makeText(this, "Failed to load products.", Toast.LENGTH_SHORT).show()
            }
        }

        // Retrieve NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)

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
}

