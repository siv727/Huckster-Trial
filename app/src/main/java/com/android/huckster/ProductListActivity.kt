package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductListView
import androidx.core.content.res.ResourcesCompat
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.startEditProductActivity
import com.android.huckster.utils.startHomeActivity

class ProductListActivity : Activity() {

    private var isAlternate = false // Flag to alternate between the backgrounds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        val listviewiew  = findViewById<ListView>(R.id.listlist)

        // Add a sample product if the list is empty (for testing)
        if (ProductData.getProducts().isEmpty()) {
            ProductData.addProduct("Product1", "kilo", 5.0, 50, R.drawable.huckster)
        }

        // Initialize adapter with dynamic product list




        val adapter = ProductListView(this, ProductData.getProducts())
        listviewiew.adapter = adapter
        val addProd : TextView = findViewById(R.id.add_something)
        addProd.setOnClickListener {
            startEditProductActivity()
        }
        val prod : TextView = findViewById(R.id.tempProd)
        prod.setOnClickListener {
            startEditProductActivity()
        }



        val button_back = findViewById<ImageView>(R.id.back_settings)
        button_back.setOnClickListener {
            Log.e("Settings", "Back to settings")
            startHomeActivity()
        }
    }

//    private fun showProductDialog(productName: String) {
//        val productDialog = ProductActivity(this, productName)
//        productDialog.show()
//    }

}
