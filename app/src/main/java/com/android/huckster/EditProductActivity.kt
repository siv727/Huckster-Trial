package com.android.huckster

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductData

class EditProductActivity(
    private val context: Context,
    private val product: Product,
    private val onProductUpdated: () -> Unit = {}
) {
    fun showDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_edit_product, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val productNameTextView: TextView = dialogView.findViewById(R.id.product_name)
        val unitSpinner: Spinner = dialogView.findViewById(R.id.unit_spinner)
        val categoryEditText: EditText = dialogView.findViewById(R.id.category_edit_text)
        val priceEditText: EditText = dialogView.findViewById(R.id.price_edit_text)
        val quantityEditText: EditText = dialogView.findViewById(R.id.stocks_edit_text)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        productNameTextView.text = product.productName
        categoryEditText.setText(product.category)
        priceEditText.setText(product.price.toString())
        quantityEditText.setText(product.quantity.toString())
        
        val unitOptions = listOf("package", "piece", "kilogram", "liter", "meter", "milliliter", "case")
        val adapter = ArrayAdapter(context, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = adapter

        // Set the current unit as selected in the spinner
        val unitIndex = unitOptions.indexOf(product.unit)
        if (unitIndex != -1) {
            unitSpinner.setSelection(unitIndex)
        }

        saveButton.setOnClickListener {
            val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: product.price
            val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: product.quantity
            val newUnit = unitSpinner.selectedItem?.toString() ?: product.unit
            val newCategory = categoryEditText.text.toString().trim()

            if (newCategory.isBlank()) {
                Toast.makeText(context, "Category cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (unitSpinner.selectedItemPosition == 0) {
                Toast.makeText(context, "Please select a valid unit!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the product in the database
            ProductData.updateProduct(
                productName = product.productName,
                newUnit = newUnit,
                newPrice = newPrice,
                newQuantity = newQuantity,
                newCategory = newCategory
            ) { success ->
                if (success) {
                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                    onProductUpdated() // Trigger the callback
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
}