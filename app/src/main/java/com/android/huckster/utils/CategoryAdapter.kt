package com.android.huckster.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.huckster.R

class CategoryAdapter(private val context: Context, private val categories: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false)
        }

        val categoryName = categories[position]
        val categoryTitle = view?.findViewById<TextView>(R.id.category_title)
        categoryTitle?.text = categoryName

        return view!!
    }
}
