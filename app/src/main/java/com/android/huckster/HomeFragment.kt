package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.UserData
import com.android.huckster.utils.refreshNotificationBadge

class HomeFragment : Fragment() {

    private lateinit var inventoryValueTextView: TextView
    private lateinit var topSellingRecyclerView: RecyclerView
    private lateinit var restockingTrendsRecyclerView: RecyclerView

    private val ADD_PRODUCT_REQUEST_CODE = 1
    private val REMOVE_PRODUCT_REQUEST_CODE = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewGreeting = view.findViewById<TextView>(R.id.greeting)
        UserData.loggedInUser?.let { user ->
            textViewGreeting.text = "Hello ${user.firstName}!"
        }

        inventoryValueTextView = view.findViewById(R.id.inventoryValueTextView)
        topSellingRecyclerView = view.findViewById(R.id.topSellingRecyclerView)
        restockingTrendsRecyclerView = view.findViewById(R.id.restockingTrendsRecyclerView)

        // Set up RecyclerViews
        topSellingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        restockingTrendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        refreshNotificationBadge()
        loadInventoryValue()
        loadTopSellingProducts()
        loadRestockingTrends()

        val addButton = view.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            startActivityForResult(Intent(requireContext(), NewProductActivity::class.java), ADD_PRODUCT_REQUEST_CODE)
        }

        val removeButton = view.findViewById<Button>(R.id.button_remove)
        removeButton.setOnClickListener{
            startActivityForResult(Intent(requireContext(), RemoveProductActivity::class.java), REMOVE_PRODUCT_REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list when the fragment resume
        refreshNotificationBadge()
    }

    // Handle result from NewProductActivity or RemoveProductActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_PRODUCT_REQUEST_CODE -> {
                    // Refresh data after adding product
                    loadInventoryValue()
                    loadTopSellingProducts()
                    loadRestockingTrends()
                }
                REMOVE_PRODUCT_REQUEST_CODE -> {
                    // Refresh data after removing product
                    loadInventoryValue()
                    loadTopSellingProducts()
                    loadRestockingTrends()
                }
            }
        }
    }

    private fun loadInventoryValue() {
        ProductData.getProducts { products ->
            val totalValue = products.sumOf { it.price * it.quantity }
            inventoryValueTextView.text = "Total Inventory Value: $${"%.2f".format(totalValue)}"
        }
    }

    private fun loadTopSellingProducts() {
        ProductData.getProducts { products ->
            // Get top 5 selling products
            val topSellingProducts = products.sortedByDescending { it.sales }.take(5)
            topSellingRecyclerView.adapter =
                ProductAdapter(topSellingProducts, "Top-Selling Products")
        }
    }

    private fun loadRestockingTrends() {
        ProductData.getProducts { products ->
            // Get top 5 frequently restocked products
            val restockingTrends = products.sortedByDescending { it.quantitySold }.take(5)
            restockingTrendsRecyclerView.adapter =
                ProductAdapter(restockingTrends, "Restocking Trends")
        }
    }
}
