package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val textview_greeting = findViewById<TextView>(R.id.greeting)

        var first_name = "";
        var last_name = "";
        var email_holder = ""
        intent?.let{
            it.getStringExtra("email")?.let{ email ->
                email_holder = email
            }

            it.getStringExtra("fname")?.let{ fname ->
                textview_greeting.setText("Hello $fname!")
                first_name = fname
            }

            it.getStringExtra("lname")?.let{ lname ->
                last_name = lname
            }
        }

        val back_button = findViewById<ImageView>(R.id.back_login)

        back_button.setOnClickListener {
            Log.e("Landing", "Successful Registration!")
            Toast.makeText(this, "Log in!", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }


        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            Log.e("Account", "Moved to account profile!")
            Toast.makeText(this, "Account Settings", Toast.LENGTH_LONG).show()

            startActivity(
                Intent(this, SettingsActivity::class.java).apply{
                    putExtra("fname", first_name)
                    putExtra("lname", last_name)
                    putExtra("email", email_holder)
                }
            )
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