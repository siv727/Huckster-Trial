package com.android.huckster.utils

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Product(
    val productName: String = "",
    var price: Double = 0.0,
    var unit: String = "",
    var quantity: Int = 0,
    var category: String = "",
    var quantitySold: Int = 0,
    val sales: Double = 0.0
)

object ProductData {
    private val database = FirebaseDatabase.getInstance().getReference("Products")
    private var cachedProducts: List<Product> = emptyList()

    // Add a new product to Firebase
    fun addProduct(
        productName: String,
        unit: String,
        price: Double,
        quantity: Int,
        category: String,
        callback: (Boolean) -> Unit
    ) {
        val product = Product(
            productName,
            price,
            unit,
            quantity,
            category,
            quantitySold = 0,
            sales = 0.0
        )
        database.child(productName).setValue(product)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Update an existing product in Firebase
    fun updateProduct(
        oldProductName: String,
        newProductName: String,
        newUnit: String,
        newPrice: Double,
        newQuantity: Int,
        newCategory: String,
        newQuantitySold: Int,
        callback: (Boolean) -> Unit
    ) {
        val newSales = newQuantitySold * newPrice

        // Remove the old product entry if the product name has changed
        if (oldProductName != newProductName) {
            database.child(oldProductName).removeValue()
        }

        val updatedProduct = Product(
            productName = newProductName,
            price = newPrice,
            unit = newUnit,
            quantity = newQuantity,
            category = newCategory,
            quantitySold = newQuantitySold,
            sales = newSales
        )

        database.child(newProductName).setValue(updatedProduct)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Remove a product from Firebase
    fun removeProduct(productName: String, callback: (Boolean) -> Unit) {
        database.child(productName).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Fetch all products from Firebase
    fun getProducts(callback: (List<Product>) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            val productList = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
            callback(productList)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }

    // Fetch low-stock products from Firebase
    fun getLowStockProducts(threshold: Int): List<Product> {
        return cachedProducts?.filter { it.quantity <= threshold } ?: emptyList()
    }


    // Preload products into memory
    fun preloadProducts(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = database.get().await()
                cachedProducts = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    // Get preloaded products
    fun getCachedProducts(): List<Product> {
        return cachedProducts
    }

    // Add a new category for products
    fun addCategory(categoryName: String, callback: (Boolean) -> Unit) {
        database.child("Categories").child(categoryName).setValue(true)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Remove a category
    fun removeCategory(categoryName: String, callback: (Boolean) -> Unit) {
        database.child("Categories").child(categoryName).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Fetch all categories
    fun getCategories(callback: (List<String>) -> Unit) {
        database.child("Categories").get()
            .addOnSuccessListener { snapshot ->
                val categories = snapshot.children.mapNotNull { it.key }
                callback(categories)
            }.addOnFailureListener {
                callback(emptyList())
            }
    }

    fun getRestockTrends(callback: (List<Product>) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            val products = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                .sortedByDescending { it.quantitySold } // Sort by quantity sold (or other metrics)
            callback(products)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}