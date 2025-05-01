package com.android.huckster

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.huckster.utils.UserData

class ChangePasswordActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val backButton = findViewById<ImageView>(R.id.back_settings)
        val currentPassword = findViewById<EditText>(R.id.edittext_current_password)
        val newPassword = findViewById<EditText>(R.id.edittext_new_password)
        val confirmPassword = findViewById<EditText>(R.id.edittext_confirm_new_password)
        val changeButton = findViewById<Button>(R.id.button_change_password)

        backButton.setOnClickListener {
            finish()
        }

        changeButton.setOnClickListener {
            val newPwd = newPassword.text.toString().trim()
            val confirmPwd = confirmPassword.text.toString().trim()

            if (newPwd.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPwd != confirmPwd) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showConfirmationDialog(newPwd)
        }

    }

    private fun showConfirmationDialog(newPwd: String) {
        val dialogView = layoutInflater.inflate(R.layout.confirm_change_password_dialog, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.confirm_button).setOnClickListener {
            UserData.changePassword(newPwd) { success, message ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        finish()
                    } else {
                        Toast.makeText(this, message ?: "Password update failed.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

}
