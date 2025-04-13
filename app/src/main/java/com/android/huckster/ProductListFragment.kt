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
import androidx.lifecycle.ViewModelProvider
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.ProductListView
import com.android.huckster.utils.SharedProductViewModel
import com.android.huckster.utils.setNotifCountImage

class ProductListFragment : Fragment() {
    private lateinit var sharedViewModel: SharedProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedProductViewModel::class.java]
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
            sharedViewModel.refreshProductList.value = true
        }

//        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
//        if (ProductData.getLowStockProductCount() != 0) {
//            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
//        }

        view.findViewById<ImageView>(R.id.back_settings).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        sharedViewModel.refreshProductList.observe(viewLifecycleOwner) { shouldRefresh ->
            if (shouldRefresh) {
                listView.adapter = ProductListView(requireContext(), ProductData.getProducts())
                sharedViewModel.refreshProductList.value = false
            }
        }
    }
}

