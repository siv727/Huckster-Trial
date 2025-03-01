package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
        // Retrieve user data from UserData
        UserData.loggedInUser?.let { user ->
            first_name = user.firstName
            last_name = user.lastName
            email_holder = user.email
            textview_greeting.text = "Hello $first_name!"
        }

/* tried using UserData class
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
*/

        val back_button = findViewById<ImageView>(R.id.back_login)

        back_button.setOnClickListener {
            showLogoutConfirmation()
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

    private fun showLogoutConfirmation() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }

        btnConfirm.setOnClickListener {
            UserData.loggedInUser = null
            dialog.dismiss()
            this.startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
        dialog.show()
    }

}