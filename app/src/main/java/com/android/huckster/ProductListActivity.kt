package com.android.huckster

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.huckster.utils.Product
import com.android.huckster.utils.ProductListView
import com.android.huckster.utils.startHomeActivity

class ProductListActivity : Activity() {

    private var isAlternate = false // Flag to alternate between the backgrounds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        val listviewiew  = findViewById<ListView>(R.id.listlist)

        val productList = listOf(
            Product("MITO",5.0,50,R.drawable.huckster),
                    Product("MITO",5.0,50,R.drawable.huckster),
            Product("MITO",5.0,50,R.drawable.huckster),
            Product("MITO",5.0,50,R.drawable.huckster),
            Product("MITO",5.0,50,R.drawable.huckster),
            Product("MITO",5.0,50,R.drawable.huckster)
        )

        val adapter = ProductListView(this,productList)
        listviewiew.adapter = adapter
        val addSomethingButton: TextView = findViewById(R.id.add_something)
//        val dynamicContainer: LinearLayout = findViewById(R.id.dynamicContainer)
//
//        addSomethingButton.setOnClickListener {
//            // Create the new LinearLayout container
//            val newLayout = LinearLayout(this)
//            val layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.setMargins(0, 40, 0, 0)  // Set marginTop as 20px
//            newLayout.layoutParams = layoutParams
//            newLayout.orientation = LinearLayout.VERTICAL
//
//            // Set the background conditionally based on the isAlternate flag
//            if (isAlternate) {
//                newLayout.setBackgroundResource(R.drawable.color_gradient_button_2) // First background
//            } else {
//                newLayout.setBackgroundResource(R.drawable.dialog_background) // Second background
//            }
//
//            // Toggle the flag to alternate backgrounds for next time
//            isAlternate = !isAlternate
//
//            // Set padding for the new layout
//            newLayout.setPadding(0, 72, 0, 72)
//
//            // Create the TextView inside the new container
//            val newTextView = TextView(this)
//            val textViewParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            newTextView.layoutParams = textViewParams
//            newTextView.gravity = Gravity.CENTER
//            newTextView.setText(R.string.product)
//            newTextView.setTextSize(24f)
//            newTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
//            newTextView.setPadding(20, 20, 20, 20)
//
//            // Set the font using ResourcesCompat for compatibility
//            val font = ResourcesCompat.getFont(this, R.font.montserrat_semi_bold)
//            newTextView.typeface = font
//
//            // Set the text color conditionally based on the background
//            if (!isAlternate) {
//                // Apply white text for the color_gradient_button_2 background
//                newTextView.setTextColor(resources.getColor(R.color.white))
//            } else {
//                // Apply trade_blue text for the dialog_background
//                newTextView.setTextColor(resources.getColor(R.color.trade_blue))
//            }
//
//            // Add the TextView to the new layout
//            newLayout.addView(newTextView)
//
//            // Add the new layout to the dynamic container
//            dynamicContainer.addView(newLayout)
//        }


        val button_back = findViewById<ImageView>(R.id.back_settings)

        button_back.setOnClickListener {
            Log.e("Settings", "Back to settings")
            startHomeActivity()
        }
    }
}
