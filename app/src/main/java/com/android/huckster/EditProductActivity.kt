package com.android.huckster

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.android.huckster.utils.Product // Import this if it's in another package
import com.android.huckster.utils.ProductData

class EditProductActivity(
    private val context: Context,
    private val product: Product,
    private val onProductUpdated: () -> Unit = {} // optional lambda with default
) {
    fun showDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_edit_product, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val productNameTextView: TextView = dialogView.findViewById(R.id.product_name)
        val unitSpinner: Spinner = dialogView.findViewById(R.id.unit_spinner)
        val priceEditText: EditText = dialogView.findViewById(R.id.price_edit_text)
        val quantityEditText: EditText = dialogView.findViewById(R.id.stocks_edit_text)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        productNameTextView.text = product.productName
        priceEditText.setText(product.price.toString())
        quantityEditText.setText(product.quantity.toString())

        val unitOptions = arrayOf("Piece", "Pack", "Box", "Kilogram")
        val adapter = ArrayAdapter(context, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = adapter

        val unitIndex = unitOptions.indexOf(product.unit)
        if (unitIndex != -1) {
            unitSpinner.setSelection(unitIndex)
        }

        saveButton.setOnClickListener {
            val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: product.price
            val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: product.quantity
            val newUnit = unitSpinner.selectedItem.toString()

            ProductData.updateProduct(product.productName, newUnit, newPrice, newQuantity)

            onProductUpdated() // <-- Callback triggered here
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

