package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.huckster.R
import com.android.huckster.utils.CategoryAdapter
import com.android.huckster.utils.ProductData

class CategoriesActivity : Activity() {

    private lateinit var categoriesListView: ListView
    private lateinit var categoryAdapter: CategoryAdapter
    private val categories: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        categoriesListView = findViewById(R.id.categories_list)

        // Fetch categories from Firebase
        fetchCategories()

        // Set the adapter
        categoryAdapter = CategoryAdapter(this, categories)
        categoriesListView.adapter = categoryAdapter

        // Back button functionality
        findViewById<ImageView>(R.id.back_categories).setOnClickListener {
            finish()
        }

        // Add Category Button functionality
        findViewById<Button>(R.id.button_add_category).setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun fetchCategories() {
        ProductData.getCategories { categoryList ->
            if (categoryList.isNotEmpty()) {
                categories.clear()
                categories.addAll(categoryList)
                categoryAdapter.notifyDataSetChanged()  // Notify the adapter about the data change
            } else {
                Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show()
            }
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
                    fetchCategories()
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

