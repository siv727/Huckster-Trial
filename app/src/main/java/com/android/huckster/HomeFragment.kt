package com.android.huckster

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import com.android.huckster.utils.UserData

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewGreeting = view.findViewById<TextView>(R.id.greeting)
        UserData.loggedInUser?.let { user ->
            textViewGreeting.text = "Hello ${user.firstName}!"
        }

        val flipper = view.findViewById<ViewFlipper>(R.id.viewFlipper)
        flipper.setInAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right))
        flipper.setOutAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left))

//        val notifCount = view.findViewById<ImageView>(R.id.notif_count)
//        if (ProductData.getLowStockProductCount() != 0) {
//            notifCount.setNotifCountImage(ProductData.getLowStockProductCount())
//        }

        val addButton = view.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            startActivity(Intent(requireContext(), NewProductActivity::class.java))
        }
    }
}
