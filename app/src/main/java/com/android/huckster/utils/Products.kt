package com.android.huckster.utils

data class Product(
    val productName: String,
    val unit: String,
    val price: Double,
    val quantity: Int
)

object ProductData {
    private val products: MutableList<Product> = mutableListOf()

    var selectedProduct: Product? = null // Stores the currently selected product

    fun addProduct(productName: String, unit: String, price: Double, quantity: Int): Boolean {
        if (products.any { it.productName == productName }) {
            return false // Product already exists
        }

        val newProduct = Product(productName, unit, price, quantity)
        products.add(newProduct)
        return true // Successfully added
    }

    fun removeProduct(productName: String): Boolean {
        return products.removeIf { it.productName == productName }
    }

    fun updateProduct(productName: String, newUnit: String, newPrice: Double, newQuantity: Int): Boolean {
        val index = products.indexOfFirst { it.productName == productName }
        if (index != -1) {
            // Update product details
            products[index] = Product(productName, newUnit, newPrice, newQuantity)
            return true
        }
        return false // Product not found
    }

    fun getProducts(): List<Product> = products.toList() // Returns a copy of the product list
}
