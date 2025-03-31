package com.android.huckster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.UserData
import com.android.huckster.utils.setNotifCountImage
import com.android.huckster.utils.startEditProductActivity

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewGreeting = view.findViewById<TextView>(R.id.greeting)

        // Retrieve user data from UserData
        UserData.loggedInUser?.let { user ->
            textViewGreeting.text = "Hello ${user.firstName}!"
        }

        val flipper = view.findViewById<ViewFlipper>(R.id.viewFlipper)
        val slideInRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val slideOutLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)

        flipper.setInAnimation(slideInRight)
        flipper.setOutAnimation(slideOutLeft)

        // Setup notification count badge if applicable
        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
        if (ProductData.getLowStockProductCount() != 0) {
            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
        }

        // Add button click listeners
        val addButton = view.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            startEditProductActivity()
        }
    }
}
