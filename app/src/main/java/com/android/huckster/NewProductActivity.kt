package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.shortToast

class NewProductActivity : Activity() {

    private lateinit var categorySpinner: Spinner
    private var categoryList: List<String> = emptyList() // List of category names
    private var categoryIdList: List<String> = emptyList() // List of corresponding category IDs
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        val unitSpinner = findViewById<Spinner>(R.id.unit_spinner)
        val productNameInput = findViewById<EditText>(R.id.product_name)
        val priceInput = findViewById<EditText>(R.id.price)
        val stockInput = findViewById<EditText>(R.id.stock_count)
        val addProductButton = findViewById<Button>(R.id.button_login)
        val buttonBack = findViewById<ImageView>(R.id.back_settings)

        categorySpinner = findViewById(R.id.category_spinner)

        // Preload the spinner with default unit values
        val unitOptions = listOf("Unit", "Package", "Piece", "Kilogram", "Liter", "Meter", "Milliliter", "Case")
        val unitAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, unitOptions) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the placeholder item
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view as TextView
                // Set the placeholder text color to gray if it's selected
                textView.setTextColor(
                    if (position == 0) resources.getColor(R.color.gray) else resources.getColor(R.color.trade_blue)
                )
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                // Set the placeholder text color to gray in the dropdown
                textView.setTextColor(
                    if (position == 0) resources.getColor(R.color.gray) else resources.getColor(R.color.trade_blue)
                )
                return view
            }
        }
        unitSpinner.adapter = unitAdapter

        // Fetch categories from Firebase
        fetchCategories()

        // Back button
        buttonBack.setOnClickListener {
            finish()
        }

        // Add Product Button Click
        addProductButton.setOnClickListener {
            val productName = productNameInput.text.toString().trim()
            val category = selectedCategoryId ?: ""
            val unit = unitSpinner.selectedItem?.toString() ?: ""
            val priceText = priceInput.text.toString().trim()
            val stockText = stockInput.text.toString().trim()

            // Validate inputs
            if (productName.isBlank() || category.isBlank() || priceText.isBlank() || stockText.isBlank() || unitSpinner.selectedItemPosition == 0) {
                shortToast("Please fill out all fields and select a valid unit and category!")
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

    // Fetch categories from Firebase and set them in the category spinner
    private fun fetchCategories() {
        ProductData.getCategories { categories ->
            val categoryNames = categories.values.toList()
            val categoryIds = categories.keys.toList()
            categoryList = categoryNames
            categoryIdList = categoryIds

            // Add the "Select Category" and "Add New Category" options
            val categoryWithAdd = listOf("Select Category") + categoryList + "Add New Category"
            val categoryAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, categoryWithAdd) {
                override fun isEnabled(position: Int): Boolean {
                    // Disable the placeholder item
                    return position != 0
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view as TextView
                    // Set the placeholder and "Add New Category" text color
                    textView.setTextColor(
                        if (position == 0 || position == categoryWithAdd.size - 1) resources.getColor(R.color.gray)
                        else resources.getColor(R.color.trade_blue)
                    )
                    return view
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view as TextView
                    // Set the placeholder and "Add New Category" text color in the dropdown
                    textView.setTextColor(
                        if (position == 0 || position == categoryWithAdd.size - 1) resources.getColor(R.color.gray)
                        else resources.getColor(R.color.trade_blue)
                    )
                    return view
                }
            }
            categorySpinner.adapter = categoryAdapter

            // Handle the "Add New Category" option selection
            categorySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when (position) {
                        0 -> {
                            selectedCategoryId = null // No category selected
                        }
                        categoryWithAdd.size - 1 -> {
                            showAddCategoryDialog()
                        }
                        else -> {
                            selectedCategoryId = categoryIdList[position - 1] // Map to category ID
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        }
    }

    // Show a dialog to add a new category
    private fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val categoryEditText: EditText = dialogView.findViewById(R.id.category_edit_text)
        val addButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        addButton.setOnClickListener {
            val categoryName = categoryEditText.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Category name cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ProductData.addCategory(categoryName) { success ->
                if (success) {
                    Toast.makeText(this, "Category added successfully!", Toast.LENGTH_SHORT).show()
                    fetchCategories()  // Reload categories after adding
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
                }
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}