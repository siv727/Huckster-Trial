package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import com.android.huckster.utils.UserData
import com.android.huckster.utils.shortToast
import com.android.huckster.utils.startNotificationsActivity
import com.android.huckster.utils.startProductListActivity
import com.android.huckster.utils.startSettingsActivity

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val textview_greeting = findViewById<TextView>(R.id.greeting)

        var first_name = "";
        var last_name = "";
        var email_holder = ""

        // Retrieve user data from UserData
        UserData.loggedInUser?.let { user ->
            first_name = user.firstName
            last_name = user.lastName
            email_holder = user.email
            textview_greeting.text = "Hello $first_name!"
        }

        val flipper = findViewById<ViewFlipper>(R.id.viewFlipper)

        val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        val slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)

        flipper.setInAnimation(slideInRight)
        flipper.setOutAnimation(slideOutLeft)

        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            startSettingsActivity()
        }

        val list_button = findViewById<LinearLayout>(R.id.nav_list)
        list_button.setOnClickListener {
            startProductListActivity()
        }

        val notif_button = findViewById<LinearLayout>(R.id.nav_notif)
        notif_button.setOnClickListener {
            startNotificationsActivity()
        }
    }
}