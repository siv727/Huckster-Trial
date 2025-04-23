package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.android.huckster.utils.shortToast

class AboutHucksterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_huckster)

        val settings_button = findViewById<ImageView>(R.id.back_settings)

        settings_button.setOnClickListener {
            finish()
        }

    }
}