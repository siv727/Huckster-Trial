package com.android.huckster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.huckster.utils.NotificationListView
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.refreshNotificationBadge

class NotificationsFragment : Fragment() {

    private lateinit var listNotifs: ListView
    private lateinit var clearText: View
    private lateinit var emptyText: TextView
    private lateinit var adapter: NotificationListView
    private var notifList: MutableList<Product> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listNotifs = view.findViewById(R.id.listview_notification1)
        clearText = view.findViewById(R.id.textview_clear)
        emptyText = view.findViewById(R.id.textview_empty)

        adapter = NotificationListView(requireContext(), notifList)
        listNotifs.adapter = adapter

        refreshNotificationBadge()
        fetchAndDisplayLowStockProducts()
        clearText.visibility = View.GONE
        clearText.setOnClickListener {
            notifList.clear()
            adapter.notifyDataSetChanged()
            emptyText.visibility = View.VISIBLE
        }
    }

    private fun fetchAndDisplayLowStockProducts() {
        val sharedPref = requireContext().getSharedPreferences("StockPrefs", android.content.Context.MODE_PRIVATE)
        val lowStockThreshold = sharedPref.getInt("low_stock_threshold", 5)

        ProductData.getLowStockProducts(lowStockThreshold) { lowStockProducts ->
            notifList.clear()
            notifList.addAll(lowStockProducts)
            adapter.notifyDataSetChanged()

            emptyText.visibility = if (notifList.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
