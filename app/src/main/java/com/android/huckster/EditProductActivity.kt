package com.android.huckster

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

class EditProductActivity(private val context: Context, private val productName: String) {

    fun showDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_edit_product, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val productNameTextView: TextView = dialogView.findViewById(R.id.product_name)
        val unitSpinner: Spinner = dialogView.findViewById(R.id.unit_spinner)
        val priceEditText: EditText = dialogView.findViewById(R.id.price_edit_text)
        val quantityEditText: EditText = dialogView.findViewById(R.id.quantity_edit_text)
        val saveButton: Button = dialogView.findViewById(R.id.save_button)
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)

        productNameTextView.text = productName

        val unitOptions = arrayOf("Piece", "Pack", "Box", "Kilogram")
        val adapter = ArrayAdapter(context, R.layout.spinner_item, unitOptions)
        unitSpinner.adapter = adapter

        saveButton.setOnClickListener {
            // Implement save logic here
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

