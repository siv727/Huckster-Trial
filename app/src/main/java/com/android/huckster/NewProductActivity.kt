package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.android.huckster.utils.startProductListActivity

class NewProductActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

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