package com.android.huckster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import com.android.huckster.utils.NotificationListView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.setNotifCountImage

class NotificationsFragment : Fragment() {

    private lateinit var listNotifs: ListView
    private lateinit var notifCount: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listNotifs = view.findViewById(R.id.listview_notification)
//        notifCount = view.findViewById(R.id.notif_count)

        // Fetch and display low-stock products
        fetchAndDisplayLowStockProducts()
    }

    private fun fetchAndDisplayLowStockProducts() {
        val lowStockThreshold = 5 // Define the threshold for low stock
        ProductData.getLowStockProducts(lowStockThreshold) { lowStockProducts ->
            // Update the ListView with low-stock products
            val adapter = NotificationListView(requireContext(), lowStockProducts)
            listNotifs.adapter = adapter

            // Update the notification count image
//            val lowStockCount = lowStockProducts.size
//            if (lowStockCount > 0) {
//                notifCount.setNotifCountImage(lowStockCount)
//                notifCount.visibility = View.VISIBLE
//            } else {
//                notifCount.visibility = View.GONE // Hide the notification icon if no low-stock products
//            }
        }
    }
}