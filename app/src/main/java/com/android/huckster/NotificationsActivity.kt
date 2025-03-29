package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.huckster.utils.NotificationListView
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.startHomeActivity
import com.android.huckster.utils.startProductListActivity
import com.android.huckster.utils.startSettingsActivity

class NotificationsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val button_back = findViewById<ImageView>(R.id.back_settings)
        button_back.setOnClickListener{
            startHomeActivity()
        }

        val list_notifs = findViewById<ListView>(R.id.listview_notification)
        val adapter = NotificationListView(this,ProductData.getLowStockProduct())
        list_notifs.adapter = adapter;

        val home_button = findViewById<LinearLayout>(R.id.nav_home)
        home_button.setOnClickListener {
            startHomeActivity()
        }

        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            startSettingsActivity()
        }

        val list_button = findViewById<LinearLayout>(R.id.nav_list)
        list_button.setOnClickListener {
            startProductListActivity()
        }

    }
}