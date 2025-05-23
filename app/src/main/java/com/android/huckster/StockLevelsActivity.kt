package com.android.huckster

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.huckster.utils.NotificationHelper
import com.android.huckster.utils.NotificationListView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.refreshNotificationBadge
import android.Manifest


class StockLevelsActivity : Activity() {

    private lateinit var listNotifs: ListView
    private lateinit var thresholdInput: EditText
    private lateinit var thresholdSeekBar: SeekBar
    private lateinit var applyThresholdButton: Button
    private lateinit var currentThresholdText: TextView
    private lateinit var settingsButton: ImageView

    private var lowStockThreshold: Int = 5 // Default threshold

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_levels)

        // Load threshold from preferences
        val sharedPref = getSharedPreferences("StockPrefs", MODE_PRIVATE)
        lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)

        // Initialize views
        listNotifs = findViewById(R.id.listview_notification2)
        thresholdInput = findViewById(R.id.threshold_input)
        thresholdSeekBar = findViewById(R.id.threshold_seekbar)
        applyThresholdButton = findViewById(R.id.apply_threshold_button)
        currentThresholdText = findViewById(R.id.current_threshold_text)
        settingsButton = findViewById(R.id.back_settings)

        // Set default SeekBar position and display
        thresholdSeekBar.progress = lowStockThreshold
        updateThresholdDisplay()

        // Back button to exit
        settingsButton.setOnClickListener {
            refreshNotificationBadge()
            finish()
        }

        // Apply threshold from user input or seek bar
        applyThresholdButton.setOnClickListener {
            val inputText = thresholdInput.text.toString()
            val inputThreshold = inputText.toIntOrNull()

            if (inputThreshold != null) {
                lowStockThreshold = inputThreshold
                thresholdSeekBar.progress = inputThreshold
            } else {
                lowStockThreshold = thresholdSeekBar.progress
            }

            saveThresholdToPreferences(lowStockThreshold)
            refreshNotificationBadge()
            fetchLowStockNotificationCount() // Fetch and show the low stock notification
            updateThresholdDisplay()
            fetchAndDisplayLowStockProducts()
        }

        // Sync SeekBar with displayed threshold
        thresholdSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                lowStockThreshold = progress
                updateThresholdDisplay()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Load products in background and show filtered result
        ProductData.preloadProducts { success ->
            runOnUiThread {
                if (success) {
                    fetchAndDisplayLowStockProducts()
                } else {
                    Toast.makeText(this, "Failed to load products.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Display only products below threshold
    private fun fetchAndDisplayLowStockProducts() {
        ProductData.getLowStockProducts(lowStockThreshold) { lowStockProducts ->
            runOnUiThread {
                if (lowStockProducts.isEmpty()) {
                    Toast.makeText(this, "No products below threshold.", Toast.LENGTH_SHORT).show()
                }
                listNotifs.adapter = NotificationListView(this, lowStockProducts)
            }
        }
    }

    private fun updateThresholdDisplay() {
        currentThresholdText.text = "Current threshold: $lowStockThreshold"
    }

    private fun saveThresholdToPreferences(threshold: Int) {
        val sharedPref = getSharedPreferences("StockPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("low_stock_threshold", threshold)
            apply()
        }
    }

    // Fetch low stock notification count and show notification
    private fun fetchLowStockNotificationCount() {
        ProductData.getLowStockNotificationCount(lowStockThreshold) { count ->
            if (count > 0) {
                val title = "$count Low Stock Alert"
                val message = "Check your stock levels!"

                // Show notification with low stock alert
                NotificationHelper.showNotification(this, title, message)
            } else {
                // No low stock, optionally hide the notification
                NotificationHelper.hideNotification(this)
            }
        }
    }
}

