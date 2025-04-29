package com.android.huckster

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.ProductListView
import com.android.huckster.utils.Product

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

        // Fetch and display products
        fetchAndDisplayProducts()

        // Item click opens Edit dialog
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = productList[position]
            val editDialog = EditProductActivity(requireContext(), selectedProduct) {
                // Callback: Refresh the product list after editing
                fetchAndDisplayProducts()
            }
            editDialog.showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list when the fragment resumes
        fetchAndDisplayProducts()
    }

    private fun fetchAndDisplayProducts() {
        ProductData.getProducts { products ->
            productList = products
            adapter = ProductListView(requireContext(), productList)
            listView.adapter = adapter
        }
    }
}