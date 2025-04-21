package com.android.huckster

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.ProductListView

class ProductListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.productlist)

        // Add a default product if the list is empty
        if (ProductData.getProducts().isEmpty()) {
            ProductData.addProduct("Coke na Coke", "Bottle", 2.0, 43, R.drawable.products_icon)
        }

        // Set adapter
        val adapter = ProductListView(requireContext(), ProductData.getProducts())
        listView.adapter = adapter

        // Add product button
        val addProd = view.findViewById<TextView>(R.id.add_something)
        addProd.setOnClickListener {
            startActivity(Intent(requireContext(), NewProductActivity::class.java))
        }

        // Back button
        view.findViewById<ImageView>(R.id.back_settings).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Item click opens Edit dialog
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = ProductData.getProducts()[position]
            val editDialog = EditProductActivity(requireContext(), selectedProduct) {
                // Callback: Refresh list after editing
                listView.adapter = ProductListView(requireContext(), ProductData.getProducts())
            }
            editDialog.showDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when fragment resumes
        view?.findViewById<ListView>(R.id.productlist)?.adapter =
            ProductListView(requireContext(), ProductData.getProducts())
    }
}


