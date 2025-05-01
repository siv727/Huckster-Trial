package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.RemovableProductAdapter

class RemoveProductActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var adapter: RemovableProductAdapter
    private var productList: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_product)

        listView = findViewById(R.id.remove_product_listview)

        ProductData.getProducts { products ->
            productList = products
            adapter = RemovableProductAdapter(this, productList)
            listView.adapter = adapter
        }

        val settingsButton = findViewById<ImageView>(R.id.back_settings)
        settingsButton.setOnClickListener { finish() }

        val removeBtn = findViewById<Button>(R.id.remove_selected_btn)
        removeBtn.setOnClickListener {
            val selected = adapter.getSelectedProducts()
            if (selected.isEmpty()) {
                Toast.makeText(this, "No products selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showConfirmationDialog(selected)
        }
    }

    private fun showConfirmationDialog(selectedProducts: List<Product>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val titleText = dialogView.findViewById<TextView>(R.id.dialog_title)
        val messageText = dialogView.findViewById<TextView>(R.id.dialog_message)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirm)

        val count = selectedProducts.size
        titleText.text = "Remove Products"
        messageText.text = "Are you sure you want to remove $count selected product(s)?"
        confirmButton.text = "Remove"

        confirmButton.setOnClickListener {
            for (product in selectedProducts) {
                ProductData.removeProduct(product.productName) { success ->
                    if (success) {
                        Toast.makeText(this, "${product.productName} removed", Toast.LENGTH_SHORT).show()
                        recreate()
                    } else {
                        Toast.makeText(this, "Failed to remove ${product.productName}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
