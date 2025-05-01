package com.android.huckster.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.huckster.R
import com.android.huckster.utils.ProductData

class CategoryAdapter(
    private val context: Context,
    private val categories: List<String>,
    private val categoryIds: List<String> // Pass category IDs to the adapter
) : BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false)
        }

        val categoryName = categories[position]
        val categoryId = categoryIds[position] // Get the category ID
        val categoryTitle = view?.findViewById<TextView>(R.id.category_title)
        val deleteButton = view?.findViewById<Button>(R.id.delete_button) // Add a delete button

        categoryTitle?.text = categoryName

        // Set a click listener for the delete button
        deleteButton?.setOnClickListener {
            showDeleteConfirmationDialog(categoryId, categoryName)
        }

        return view!!
    }

    // Show confirmation dialog before deleting
    private fun showDeleteConfirmationDialog(categoryId: String, categoryName: String) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_layout_delete, null)

        val title = dialogView.findViewById<TextView>(R.id.dialog_title)
        val message = dialogView.findViewById<TextView>(R.id.dialog_message)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        title.text = "Delete Category"
        message.text = "Are you sure you want to delete \"$categoryName\"?"

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            ProductData.removeCategory(categoryId) { success ->
                if (success) {
                    Toast.makeText(context, "Category removed successfully!", Toast.LENGTH_SHORT).show()
                    (context as? Activity)?.runOnUiThread {
                        // Refresh the list (optional: could also be done via callback)
                        (context as? Activity)?.recreate()
                    }
                } else {
                    Toast.makeText(context, "Failed to remove category", Toast.LENGTH_SHORT).show()
                }
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    // Remove the category using the helper method
    private fun removeCategory(categoryId: String) {
        ProductData.removeCategory(categoryId) { success ->
            if (success) {
                Toast.makeText(context, "Category removed successfully!", Toast.LENGTH_SHORT).show()
                // Notify the adapter that the data set has changed
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Failed to remove category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
