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
        val acc_button = findViewById<LinearLayout>(R.id.nav_account)
        acc_button.setOnClickListener {
            Log.e("Account", "Moved to account profile!")

            startSettingsActivity()
/*
            `putExtra` redundant code-- already have userdata
            startActivity(
                Intent(this, SettingsActivity::class.java).apply{
                    putExtra("fname", first_name)
                    putExtra("lname", last_name)
                    putExtra("email", email_holder)
                }
            )

 */
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

      //Reserved for later stages of development
        val list_button = findViewById<LinearLayout>(R.id.nav_list)
        list_button.setOnClickListener {
            Log.e("Product List", "Moved to product list!")
            startProductListActivity()
        }

/*
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