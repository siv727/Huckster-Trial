package com.android.huckster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.result.contract.ActivityResultContracts
import com.android.huckster.utils.ProductData
import com.android.huckster.utils.ProductListView
import com.android.huckster.utils.UserData
import com.android.huckster.utils.setNotifCountImage
import com.android.huckster.utils.startEditProductActivity

class HomeFragment : Fragment() {
    // Please put this in Extensions
    private val addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Refresh product list when a new product is successfully added
            updateProductList()
        }
    }

    // Also this one in Extensions
    private fun updateProductList() {
        val listView = view?.findViewById<ListView>(R.id.listlist)
        listView?.adapter = ProductListView(requireContext(), ProductData.getProducts())
    }
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
            val intent = Intent(requireActivity(), NewProductActivity::class.java)
            addProductLauncher.launch(intent)
        }
    }
}
