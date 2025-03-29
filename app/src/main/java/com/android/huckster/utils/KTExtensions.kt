package com.android.huckster.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.huckster.AboutHucksterActivity
import com.android.huckster.DeveloperPageActivity
import com.android.huckster.NewProductActivity
import com.android.huckster.HomeActivity
import com.android.huckster.LoginActivity
import com.android.huckster.NotificationsActivity
import com.android.huckster.ProductListActivity
import com.android.huckster.ProfileActivity
import com.android.huckster.R
import com.android.huckster.RegisterActivity
import com.android.huckster.SettingsActivity

fun Activity.shortToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Activity.longToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Activity.startRegisterActivity() {
    startActivity(Intent(this, RegisterActivity::class.java))
}

fun Activity.startHomeActivity(){
    startActivity(Intent(this, HomeActivity::class.java))
    finishAffinity()
}

fun Activity.startSettingsActivity(){
    Log.e("Settings","Moved to Settings!")
    startActivity(Intent(this, SettingsActivity::class.java))
}

fun Activity.startLoginActivity(){
    Log.e("Settings","Moved to Login!")
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.startProfileActivity(){
    Log.e("Settings","Moved to Account Profile Page!")
    startActivity(Intent(this, ProfileActivity::class.java))
}

fun Activity.startDeveloperPageActivity(){
    Log.e("Settings","Moved to Developer Page!")
    startActivity(Intent(this, DeveloperPageActivity::class.java))
}

fun Activity.startAboutHucksterActivity(){
    Log.e("Settings","Moved to About Huckster Page!")
    startActivity(Intent(this,AboutHucksterActivity::class.java))
}

fun Activity.startProductListActivity(){
    Log.e("Settings","Moved to Product List!")
    startActivity(Intent(this,ProductListActivity::class.java))
}

fun Activity.startEditProductActivity(){
    Log.e("Settings","Moved to Edit Product!")
    startActivity(Intent(this,NewProductActivity::class.java))
}

fun Activity.startNotificationsActivity(){
    Log.e("Settings","Moved to Notifications!")
    startActivity(Intent(this,NotificationsActivity::class.java))
}

fun ImageView.setNotifCountImage(count : Int){
    when (count) {
        1 -> this.setImageResource(R.drawable.notif_1)
        2 -> this.setImageResource(R.drawable.notif_2)
        3 -> this.setImageResource(R.drawable.notif_3)
        4 -> this.setImageResource(R.drawable.notif_4)
        5 -> this.setImageResource(R.drawable.notif_5)
        6 -> this.setImageResource(R.drawable.notif_6)
        7 -> this.setImageResource(R.drawable.notif_7)
        8 -> this.setImageResource(R.drawable.notif_8)
        9 -> this.setImageResource(R.drawable.notif_9)
        else -> this.setImageResource(R.drawable.notif_10)
    }
}