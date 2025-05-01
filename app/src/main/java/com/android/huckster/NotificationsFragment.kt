package com.android.huckster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.huckster.utils.NotificationListView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.setNotifCountImage

class NotificationsFragment : Fragment() {

    private lateinit var listNotifs: ListView
    private lateinit var clearText: View
    private lateinit var adapter: NotificationListView

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
        clearText = view.findViewById(R.id.textview_clear)

        fetchAndDisplayLowStockProducts()

        val emptyText = view.findViewById<TextView>(R.id.textview_empty)
        clearText.setOnClickListener {
            adapter.clearList()
            emptyText.visibility = View.VISIBLE
        }

    }

    private fun fetchAndDisplayLowStockProducts() {
        val sharedPref = requireContext().getSharedPreferences("StockPrefs", android.content.Context.MODE_PRIVATE)
        val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)

        val lowStockProducts = ProductData.getLowStockProducts(lowStockThreshold)
        adapter = NotificationListView(requireContext(), lowStockProducts)
        listNotifs.adapter = adapter
    }
}


