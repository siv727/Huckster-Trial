package com.android.huckster

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
        if (ProductData.getProducts().isEmpty()) {
            ProductData.addProduct("Coke na Coke", "Bottle", 2.0, 43, R.drawable.products_icon)
        }
        listView.adapter = ProductListView(requireContext(), ProductData.getProducts())

        val addProd = view.findViewById<TextView>(R.id.add_something)
        addProd.setOnClickListener {
            startActivity(Intent(requireContext(), NewProductActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.back_settings).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResume() {
        val listView = view?.findViewById<ListView>(R.id.productlist)
        super.onResume()
        listView?.adapter = ProductListView(requireContext(), ProductData.getProducts())
    }
}

