package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.startProductListActivity
import com.android.huckster.R
import com.android.huckster.utils.shortToast

class NewProductActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        val unitSpinner = findViewById<Spinner>(R.id.unit_spinner)
        val productNameInput = findViewById<EditText>(R.id.product_name)
        val priceInput = findViewById<EditText>(R.id.price)
        val stockInput= findViewById<EditText>(R.id.stock_count)
        val addProductButton= findViewById<Button>(R.id.button_login)
        val buttonBack = findViewById<ImageView>(R.id.back_settings)

        // Unit options
        val unitOptions = arrayOf("Piece", "Pack", "Box", "Kilogram")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = adapter

        // Back button
        buttonBack.setOnClickListener {
            finish()
        }

        // Add Product Button Click
        addProductButton.setOnClickListener {
            val productName = productNameInput.text.toString().trim()
            val unit = unitSpinner.selectedItem.toString()
            val priceText = priceInput.text.toString().trim()
            val stockText = stockInput.text.toString().trim()

            // Validate inputs
            if (productName.isBlank() || priceText.isBlank() || stockText.isBlank()) {
                shortToast("Fill Out All Fields, Please")
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            val stock = stockText.toIntOrNull()

            if (price == null || price <= 0) {
                shortToast("Price must be a Positive Number")
                return@setOnClickListener
            }

            if (stock == null || stock <= 0) {
                shortToast("Stock must be a Positive Number");
                return@setOnClickListener
            }

            // Add product to ProductData
            val success = ProductData.addProduct(productName, unit, price, stock, R.drawable.huckster)

            if (success) {
                shortToast("Product Added!")
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Go back to product list
            } else {
                shortToast("Product Already Exists");
            }
        }
    }
}
