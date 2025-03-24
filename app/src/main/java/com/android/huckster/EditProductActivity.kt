package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.huckster.utils.startHomeActivity
import com.android.huckster.utils.startProductListActivity

class EditProductActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val unitSpinner: Spinner = findViewById(R.id.unit_spinner)

        val unitOptions = arrayOf("Piece", "Pack", "Box", "Kilogram")

        val adapter = ArrayAdapter(this, R.layout.spinner_item, unitOptions)

        unitSpinner.adapter = adapter

        val button_back = findViewById<ImageView>(R.id.back_settings)
        button_back.setOnClickListener {
            Log.e("Settings", "Back to settings")
            startProductListActivity()
        }
    }
}