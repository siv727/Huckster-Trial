package com.android.huckster.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.huckster.AboutHucksterActivity
import com.android.huckster.DeveloperPageActivity
import com.android.huckster.NewProductActivity
import com.android.huckster.LoginActivity
import com.android.huckster.MainContainerActivity
import com.android.huckster.ProfileActivity
import com.android.huckster.R
import com.android.huckster.RegisterActivity

fun Activity.shortToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Activity.longToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Activity.startRegisterActivity() {
    startActivity(Intent(this, RegisterActivity::class.java))
}

fun Activity.startLoginActivity(){
    Log.e("Settings","Moved to Login!")
    startActivity(Intent(this, LoginActivity::class.java))
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

// Navigation extension functions
fun Fragment.navigateToFragment(destinationId: Int) {
    findNavController().navigate(destinationId)
}

fun Fragment.startProfileActivity() {
    startActivity(Intent(requireContext(), ProfileActivity::class.java))
}

fun Fragment.startAboutHucksterActivity() {
    startActivity(Intent(requireContext(), AboutHucksterActivity::class.java))
}

fun Fragment.startDeveloperPageActivity() {
    startActivity(Intent(requireContext(), DeveloperPageActivity::class.java))
}

// For starting MainContainerActivity with specific destination
fun Context.startMainContainerActivity(startDestination: Int? = null) {
    val intent = Intent(this, MainContainerActivity::class.java)
    startDestination?.let {
        intent.putExtra("startDestination", it)
    }
    startActivity(intent)
}

fun Context.refreshNotificationBadge() {
    if (this is MainContainerActivity) {
        this.updateNotificationBadge()
    }
}

fun Fragment.refreshNotificationBadge() {
    (requireActivity() as? MainContainerActivity)?.updateNotificationBadge()
}
