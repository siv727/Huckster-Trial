package com.android.huckster

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.ProductListView
import com.android.huckster.utils.refreshNotificationBadge

class ProductListFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ProductListView
    private var productList: List<Product> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.productlist)

        // Add Product button
        val addProd = view.findViewById<TextView>(R.id.add_something)
        addProd.setOnClickListener {
            startActivity(Intent(requireContext(), NewProductActivity::class.java))
        }

        // Populate the product list from cached data
        fetchAndDisplayProducts()

        // Item click opens Edit dialog
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = productList[position]
            val editDialog = EditProductActivity(requireContext(), selectedProduct) {
                // Refresh the product list after editing
                val sharedPref = requireContext().getSharedPreferences("StockPrefs", android.content.Context.MODE_PRIVATE)
                val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)
                ProductData.getProducts { products ->
                    productList = products
                    adapter = ProductListView(requireContext(), productList, lowStockThreshold)
                    listView.adapter = adapter
                }
                fetchAndDisplayProducts()
            }
            editDialog.showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list when the fragment resumes
        fetchAndDisplayProducts()
        refreshNotificationBadge()
    }

    private fun fetchAndDisplayProducts() {
        val sharedPref = requireContext().getSharedPreferences("StockPrefs", android.content.Context.MODE_PRIVATE)
        val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5) // default is 5

        ProductData.getProducts { products ->
            productList = products
            adapter = ProductListView(requireContext(), productList, lowStockThreshold)
            listView.adapter = adapter
        }
    }

}