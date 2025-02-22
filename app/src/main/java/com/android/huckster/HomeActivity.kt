package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val back_button = findViewById<ImageView>(R.id.back_login)

        back_button.setOnClickListener {
            Log.e("Landing", "Successful Registration!")
            Toast.makeText(this, "Log in!", Toast.LENGTH_LONG).show()

            val login_intent = Intent(this, LoginActivity::class.java)
            startActivity(login_intent)
        }

        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            Log.e("Account", "Moved to account profile!")
            Toast.makeText(this, "Account Settings", Toast.LENGTH_LONG).show()

            val acc_intent = Intent(this, SettingsActivity::class.java)
            startActivity(acc_intent)
        }

/*      Idk if this is needed honestly
        val home_button = findViewById<LinearLayout>(R.id.nav_home)
        home_button.setOnClickListener {
            Log.e("Home", "Moved to home!")
            Toast.makeText(this, "Home Page", Toast.LENGTH_LONG).show()

            val home_intent = Intent(this, HomeActivity::class.java)
            startActivity(home_intent)
        }
*/

/*      Reserved for later stages of development
        val list_button = findViewById<LinearLayout>(R.id.nav_list)
        list_button.setOnClickListener {
            Log.e("Product List", "Moved to product list!")
            Toast.makeText(this, "Product List", Toast.LENGTH_LONG).show()

            val prodlist_intent = Intent(this, ProductListActivity::class.java)
            startActivity(prodlist_intent)
        }

        val notif_button = findViewById<LinearLayout>(R.id.nav_notif)
        notif_button.setOnClickListener {
            Log.e("Notifications", "Moved to notifications!")
            Toast.makeText(this, "Notifications", Toast.LENGTH_LONG).show()

            val notif_intent = Intent(this, NotificationActivity::class.java)
            startActivity(notif_intent)
        }
*/

    }
}