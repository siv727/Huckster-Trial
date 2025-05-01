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
        ProductData.getCategories { categories ->
            val categoryList = categories.toMutableList()
            categoryList.add("Add Category...")

            val categoryAdapter = ArrayAdapter(context, R.layout.spinner_item, categoryList)
            categorySpinner.adapter = categoryAdapter

            val currentCategoryIndex = categoryList.indexOf(product.category).takeIf { it >= 0 } ?: 0
            categorySpinner.setSelection(currentCategoryIndex)

            categorySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    if (categoryList[position] == "Add Category...") {
                        showAddCategoryDialog(categorySpinner, categoryAdapter, categoryList)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        }

        saveButton.setOnClickListener {
            val newProductName = productNameEditText.text.toString().trim()
            val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: product.price
            val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: product.quantity
            val newSold = soldEditText.text.toString().toIntOrNull() ?: product.quantitySold
            val newUnit = unitSpinner.selectedItem?.toString() ?: product.unit
            val newCategory = categorySpinner.selectedItem?.toString() ?: product.category

            if (newCategory.isBlank() || newCategory == "Add Category...") {
                Toast.makeText(context, "Please select or add a valid category!", Toast.LENGTH_SHORT).show()
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
                newCategory = newCategory,
                newQuantitySold = newSold
            ) { success ->
                progressDialog.dismiss()
                if (success) {
                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                    onProductUpdated()
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

    private fun showAddCategoryDialog(
        spinner: Spinner,
        adapter: ArrayAdapter<String>,
        categoryList: MutableList<String>
    ) {
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
                            categoryList.add(categoryList.size - 1, newCategory)
                            adapter.notifyDataSetChanged()
                            spinner.setSelection(categoryList.indexOf(newCategory))
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
