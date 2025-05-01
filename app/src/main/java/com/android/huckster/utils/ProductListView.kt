package com.android.huckster.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.huckster.R

class ProductListView(
    private val context : Context,
    private val productList : List<Product>,
    private val lowStockThreshold : Int
) : BaseAdapter(){
    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_product_view, parent,false)

        val productPic = view.findViewById<ImageView>(R.id.product_photo)
        val productName = view.findViewById<TextView>(R.id.item_name)
        val productPrice = view.findViewById<TextView>(R.id.item_price)
        val productStock = view.findViewById<TextView>(R.id.item_stock)

        val product = productList[position]

        productPic.setImageResource(R.drawable.products_icon)
        productName.setText("${product.productName}")
        productPrice.setText("\$${product.price}")
        productStock.setText(("${product.quantity} ${product.unit}(s)"))

        if (product.quantity <= lowStockThreshold) {
            productStock.setTextColor(ContextCompat.getColor(context, R.color.reddish_white))
            productStock.text = "${product.quantity} ${product.unit}(s) left!"
        } else if (product.quantity >= 50 + lowStockThreshold) {
            productStock.setTextColor(ContextCompat.getColor(context, R.color.green_thyme))
        }


        return view
    }
}