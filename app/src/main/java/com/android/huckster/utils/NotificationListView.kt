package com.android.huckster.utils





import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.huckster.R



class NotificationListView(
    private val context : Context,
    private val productList : List<Product>

) : BaseAdapter(){
    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView?: LayoutInflater.from(context)
            .inflate(R.layout.notification_list_view_item, parent,false)

        val productPic = view.findViewById<ImageView>(R.id.product_photo)
        val productName = view.findViewById<TextView>(R.id.item_name)
        val productPrice = view.findViewById<TextView>(R.id.item_price)
        val productStock = view.findViewById<TextView>(R.id.item_stock)

        val product = productList[position]

        productPic.setImageResource(R.drawable.notification_icon)
        productName.setText("${product.productName}")
        productPrice.setText("Price: \$${product.price}")
        productStock.setText(("${product.quantity}${product.unit}(s) left!"))

        return view
    }
}