package com.android.huckster.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.huckster.R

class RemovableProductAdapter(
    private val context: Context,
    private val products: List<Product>
) : BaseAdapter() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Product = products[position]

    override fun getItemId(position: Int): Long = position.toLong()

    fun getSelectedProducts(): List<Product> {
        return selectedPositions.map { products[it] }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_product_checkbox, parent, false)

        val checkBox = view.findViewById<CheckBox>(R.id.product_checkbox)
        val nameText = view.findViewById<TextView>(R.id.product_name_checkbox)
        val priceText = view.findViewById<TextView>(R.id.product_price_checkbox)
        val stockText = view.findViewById<TextView>(R.id.product_stock_checkbox)
        val productPhoto = view.findViewById<ImageView>(R.id.product_photo)


        val product = products[position]
        nameText.text = product.productName
        priceText.text = "₱${product.price}"
        stockText.text = "${product.quantity} ${product.unit}/(s)"
        checkBox.isChecked = selectedPositions.contains(position)
        productPhoto.setImageResource(R.drawable.products_icon)

        // Set listener to toggle checkbox on click
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedPositions.add(position)
            else selectedPositions.remove(position)
        }

        // Set listener to toggle checkbox when clicking anywhere on the item
        view.setOnClickListener {
            val isChecked = !checkBox.isChecked
            checkBox.isChecked = isChecked
            if (isChecked) selectedPositions.add(position)
            else selectedPositions.remove(position)
        }

        return view
    }
}

