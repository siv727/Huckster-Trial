package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast

class DeveloperPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_page)

        val settings_button = findViewById<ImageView>(R.id.back_settings)

        settings_button.setOnClickListener {
            Log.e("Settings", "Back to settings")
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }

    }
}