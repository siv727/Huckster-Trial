package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.shortToast

class NewProductActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        val unitSpinner = findViewById<Spinner>(R.id.unit_spinner)
        val categoryInput = findViewById<EditText>(R.id.category)
        val productNameInput = findViewById<EditText>(R.id.product_name)
        val priceInput = findViewById<EditText>(R.id.price)
        val stockInput = findViewById<EditText>(R.id.stock_count)
        val addProductButton = findViewById<Button>(R.id.button_login)
        val buttonBack = findViewById<ImageView>(R.id.back_settings)

        // Preload the spinner with default unit values
        val unitOptions = listOf("Unit", "Package", "Piece", "Kilogram", "Liter", "Meter", "Milliliter", "Case")
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, unitOptions) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the placeholder item
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view as TextView
                // Set the placeholder text color to gray if it's selected
                textView.setTextColor(
                    if (position == 0) resources.getColor(R.color.gray) else resources.getColor(R.color.black)
                )
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                // Set the placeholder text color to gray in the dropdown
                textView.setTextColor(
                    if (position == 0) resources.getColor(R.color.gray) else resources.getColor(R.color.black)
                )
                return view
            }
        }
        unitSpinner.adapter = adapter

        // Back button
        buttonBack.setOnClickListener {
            finish()
        }

        // Add Product Button Click
        addProductButton.setOnClickListener {
            val productName = productNameInput.text.toString().trim()
            val category = categoryInput.text.toString().trim()
            val unit = unitSpinner.selectedItem?.toString() ?: ""
            val priceText = priceInput.text.toString().trim()
            val stockText = stockInput.text.toString().trim()

            // Validate inputs
            if (productName.isBlank() || category.isBlank() || priceText.isBlank() || stockText.isBlank() || unitSpinner.selectedItemPosition == 0) {
                shortToast("Please fill out all fields and select a valid unit!")
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            val stock = stockText.toIntOrNull()

            if (price == null || price <= 0) {
                shortToast("Price must be a positive number.")
                return@setOnClickListener
            }

            if (stock == null || stock <= 0) {
                shortToast("Stock must be a positive number.")
                return@setOnClickListener
            }

            // Add product to ProductData
            ProductData.addProduct(productName, unit, price, stock, category) { success ->
                if (success) {
                    shortToast("Product added successfully!")
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish() // Go back to product list
                } else {
                    shortToast("Product already exists!")
                }
            }
        }
    }

//    override fun finish() {
//        // Prevent adding a product if the user presses Back without clicking Add Product
//        if (!isAddingProduct) {
//            setResult(Activity.RESULT_CANCELED)
//        }
//        super.finish()
//    }
}