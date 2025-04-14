package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.huckster.utils.shortToast
import com.android.huckster.utils.startSettingsActivity

class DeveloperPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_page)

        val settings_button = findViewById<ImageView>(R.id.back_settings)

        settings_button.setOnClickListener {
            finish()
        }

    }
}