package com.android.huckster

import android.app.AlertDialog
import android.app.ProgressDialog
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

        val productNameEditText: EditText = dialogView.findViewById(R.id.name_edit_text)
        val unitSpinner: Spinner = dialogView.findViewById(R.id.unit_spinner)
        val categoryEditText: EditText = dialogView.findViewById(R.id.category_edit_text)
        val priceEditText: EditText = dialogView.findViewById(R.id.price_edit_text)
        val quantityEditText: EditText = dialogView.findViewById(R.id.stocks_edit_text)
        val soldEditText: EditText = dialogView.findViewById(R.id.sold_edit_text)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        productNameEditText.setText(product.productName)
        categoryEditText.setText(product.category)
        priceEditText.setText(product.price.toString())
        quantityEditText.setText(product.quantity.toString())
        soldEditText.setText(product.quantitySold.toString())

        val unitOptions = listOf("Package", "Piece", "Kilogram", "Liter", "Meter", "Milliliter", "Case")
        val adapter = ArrayAdapter(context, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = adapter

        // Set the current unit as selected in the spinner
        val unitIndex = unitOptions.indexOf(product.unit)
        if (unitIndex != -1) {
            unitSpinner.setSelection(unitIndex)
        }

        saveButton.setOnClickListener {
            val newProductName = productNameEditText.text.toString().trim()
            val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: product.price
            val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: product.quantity
            val newSold = soldEditText.text.toString().toIntOrNull() ?: product.quantitySold
            val newUnit = unitSpinner.selectedItem?.toString() ?: product.unit
            val newCategory = categoryEditText.text.toString().trim()

            if (newCategory.isBlank()) {
                Toast.makeText(context, "Category cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show a progress dialog while updating the product
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Updating product...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Update the product in the database
            ProductData.updateProduct(
                oldProductName = product.productName,
                newProductName = newProductName,
                newUnit = newUnit,
                newPrice = newPrice,
                newQuantity = newQuantity,
                newCategory = newCategory,
                newQuantitySold = newSold
            ) { success ->
                progressDialog.dismiss() // Dismiss the progress dialog after update
                if (success) {
                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                    onProductUpdated() // Trigger the callback to refresh the product list
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