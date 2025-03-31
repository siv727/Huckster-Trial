package com.android.huckster

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
import com.android.huckster.utils.setNotifCountImage
import com.android.huckster.utils.startEditProductActivity

class ProductListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listlist)

        // Add a sample product if the list is empty (for testing)
        if (ProductData.getProducts().isEmpty()) {
            ProductData.addProduct("Coke na Coke", "Bottle", 2.0, 43, R.drawable.products_icon)
        }

        val adapter = ProductListView(requireContext(), ProductData.getProducts())
        listView.adapter = adapter

        val addProd: TextView = view.findViewById(R.id.add_something)
        addProd.setOnClickListener {
            startEditProductActivity()
        }

        // Setup notification count badge
        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
        if (ProductData.getLowStockProductCount() != 0) {
            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
        }

        // Back button is optional with bottom navigation
        val buttonBack = view.findViewById<ImageView>(R.id.back_settings)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
