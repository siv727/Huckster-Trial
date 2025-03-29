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
import com.android.huckster.utils.startNotificationsActivity
import com.android.huckster.utils.startProductListActivity
import com.android.huckster.utils.startSettingsActivity

class ProductListActivity : Activity() {

    private var isAlternate = false // Flag to alternate between the backgrounds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        val listviewiew  = findViewById<ListView>(R.id.listlist)

        // Add a sample product if the list is empty (for testing)
        if (ProductData.getProducts().isEmpty()) {
            ProductData.addProduct("Coke na Coke", "Bottle", 2.0, 43, R.drawable.products_icon)
        }


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

        val home_button = findViewById<LinearLayout>(R.id.nav_home)
        home_button.setOnClickListener {
            startHomeActivity()
        }

        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            startSettingsActivity()
        }



        val notif_button = findViewById<LinearLayout>(R.id.nav_notif)
        notif_button.setOnClickListener {
            startNotificationsActivity()
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
