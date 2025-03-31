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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listNotifs = view.findViewById<ListView>(R.id.listview_notification)
        val adapter = NotificationListView(requireContext(), ProductData.getLowStockProduct())
        listNotifs.adapter = adapter

        // Display notification count
        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
        if (ProductData.getLowStockProductCount() != 0) {
            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
        }

        // Back button handling
        val buttonBack = view.findViewById<ImageView>(R.id.back_settings)
        buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
