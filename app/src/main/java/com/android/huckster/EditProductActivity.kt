package com.android.huckster

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.refreshNotificationBadge


class EditProductActivity(
    private val context: Context,
    private val product: Product,
    private val onProductUpdated: () -> Unit = {}
) {
    private var categoryList: List<String> = emptyList()  // List of category names
    private var categoryIdList: List<String> = emptyList()  // List of corresponding category IDs
    private var selectedCategoryId: String? = product.categoryId  // Default to product's current categoryId

    fun showDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_edit_product, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val productNameEditText: EditText = dialogView.findViewById(R.id.name_edit_text)
        val unitSpinner: Spinner = dialogView.findViewById(R.id.unit_spinner)
        val categorySpinner: Spinner = dialogView.findViewById(R.id.category_spinner)
        val priceEditText: EditText = dialogView.findViewById(R.id.price_edit_text)
        val quantityEditText: EditText = dialogView.findViewById(R.id.stocks_edit_text)
        val soldEditText: EditText = dialogView.findViewById(R.id.sold_edit_text)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        productNameEditText.setText(product.productName)
        priceEditText.setText(product.price.toString())
        quantityEditText.setText(product.quantity.toString())
        soldEditText.setText(product.quantitySold.toString())

        // Unit Spinner
        val unitOptions = listOf("Package", "Piece", "Kilogram", "Liter", "Meter", "Milliliter", "Case")
        val unitAdapter = ArrayAdapter(context, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = unitAdapter
        unitSpinner.setSelection(unitOptions.indexOf(product.unit).takeIf { it >= 0 } ?: 0)

        // Load Categories from Firebase
        fetchCategories(categorySpinner)

        saveButton.setOnClickListener {
            val newProductName = productNameEditText.text.toString().trim()
            val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: product.price
            val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: product.quantity
            val newSold = soldEditText.text.toString().toIntOrNull() ?: product.quantitySold
            val newUnit = unitSpinner.selectedItem?.toString() ?: product.unit

            if (selectedCategoryId.isNullOrBlank()) {
                Toast.makeText(context, "Please select a valid category!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Updating product...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            ProductData.updateProduct(
                oldProductName = product.productName,
                newProductName = newProductName,
                newUnit = newUnit,
                newPrice = newPrice,
                newQuantity = newQuantity,
                newCategoryId = selectedCategoryId!!,  // Pass categoryId
                newQuantitySold = newSold
            ) { success ->
                progressDialog.dismiss()
                if (success) {
                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                    onProductUpdated()
                    context.refreshNotificationBadge()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Failed to update product.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun fetchCategories(categorySpinner: Spinner) {
        ProductData.getCategories { categories ->
            categoryList = categories.values.toList()
            categoryIdList = categories.keys.toList()

            val categoryWithAdd = listOf("Select Category") + categoryList + "Add New Category"
            val categoryAdapter = object : ArrayAdapter<String>(context, R.layout.spinner_item, categoryWithAdd) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0  // Disable "Select Category"
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view as TextView
                    textView.setTextColor(
                        if (position == 0 || position == categoryWithAdd.size - 1) context.resources.getColor(R.color.gray)
                        else context.resources.getColor(R.color.black)
                    )
                    return view
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view as TextView
                    textView.setTextColor(
                        if (position == 0 || position == categoryWithAdd.size - 1) context.resources.getColor(R.color.gray)
                        else context.resources.getColor(R.color.black)
                    )
                    return view
                }
            }

            categorySpinner.adapter = categoryAdapter

            // Set default selection
            val currentCategoryIndex = categoryIdList.indexOf(product.categoryId).takeIf { it >= 0 }?.plus(1) ?: 0
            categorySpinner.setSelection(currentCategoryIndex)

            // Handle "Add New Category" option
            categorySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when (position) {
                        0 -> selectedCategoryId = null  // No category selected
                        categoryWithAdd.size - 1 -> showAddCategoryDialog()
                        else -> selectedCategoryId = categoryIdList[position - 1]  // Map to categoryId
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        }
    }

    private fun showAddCategoryDialog() {
        val input = EditText(context)
        input.hint = "Enter new category"
        input.setPadding(20, 20, 20, 20)

        AlertDialog.Builder(context)
            .setTitle("Add New Category")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val newCategory = input.text.toString().trim()
                if (newCategory.isNotEmpty()) {
                    ProductData.addCategory(newCategory) { success ->
                        if (success) {
                            fetchCategories(categorySpinner = Spinner(context)) // Reload categories
                        } else {
                            Toast.makeText(context, "Failed to add category.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}