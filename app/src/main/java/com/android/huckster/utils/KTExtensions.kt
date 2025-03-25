package com.android.huckster.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.android.huckster.AboutHucksterActivity
import com.android.huckster.DeveloperPageActivity
import com.android.huckster.NewProductActivity
import com.android.huckster.HomeActivity
import com.android.huckster.LoginActivity
import com.android.huckster.ProductListActivity
import com.android.huckster.ProfileActivity
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
    startActivity(Intent(this, SettingsActivity::class.java))
}

fun Activity.startLoginActivity(){
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.startProfileActivity(){
    startActivity(Intent(this, ProfileActivity::class.java))
}

fun Activity.startDeveloperPageActivity(){
    startActivity(Intent(this, DeveloperPageActivity::class.java))
}

fun Activity.startAboutHucksterActivity(){
    startActivity(Intent(this,AboutHucksterActivity::class.java))
}

fun Activity.startProductListActivity(){
    startActivity(Intent(this,ProductListActivity::class.java))
}

fun Activity.startEditProductActivity(){
    startActivity(Intent(this,NewProductActivity::class.java))
}