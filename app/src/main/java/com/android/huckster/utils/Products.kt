package com.android.huckster.utils

import com.google.firebase.auth.FirebaseAuth
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
    var categoryId: String = "",
    var quantitySold: Int = 0,
    val sales: Double = 0.0,
    val userId: String = ""
)

object ProductData {
    private val productDatabase = FirebaseDatabase.getInstance().getReference("Products")
    private val categoryDatabase = FirebaseDatabase.getInstance().getReference("Categories")
    private var cachedProducts: List<Product> = emptyList()

    // Add a new product to Firebase
    fun addProduct(
        productName: String,
        unit: String,
        price: Double,
        quantity: Int,
        categoryId: String, // Use categoryId instead of category name
        callback: (Boolean) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val product = Product(
            productName,
            price,
            unit,
            quantity,
            categoryId, // Store categoryId
            quantitySold = 0,
            sales = 0.0,
            userId = userId
        )
        productDatabase.child(userId).child(productName).setValue(product)
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
        newCategoryId: String,
        newQuantitySold: Int,
        callback: (Boolean) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newSales = newQuantitySold * newPrice

        // Remove the old product entry if the product name has changed
        if (oldProductName != newProductName) {
            productDatabase.child(userId).child(oldProductName).removeValue()
        }

        val updatedProduct = Product(
            productName = newProductName,
            price = newPrice,
            unit = newUnit,
            quantity = newQuantity,
            categoryId = newCategoryId,
            quantitySold = newQuantitySold,
            sales = newSales,
            userId = userId
        )

        productDatabase.child(userId).child(newProductName).setValue(updatedProduct)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Remove a product from Firebase
    fun removeProduct(productName: String, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        productDatabase.child(userId).child(productName).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Fetch all products from Firebase
    fun getProducts(callback: (List<Product>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(emptyList())
        productDatabase.child(userId).get().addOnSuccessListener { snapshot ->
            val productList = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
            callback(productList)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }

    // Fetch low-stock products from Firebase
    fun getLowStockProducts(threshold: Int, callback: (List<Product>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(emptyList())
        productDatabase.child(userId).get().addOnSuccessListener { snapshot ->
            val lowStockProducts = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                .filter { it.quantity <= threshold }
            callback(lowStockProducts)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }

    // Get notification count for low-stock products
    fun getLowStockNotificationCount(threshold: Int, callback: (Int) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(0)
        productDatabase.child(userId).get().addOnSuccessListener { snapshot ->
            val lowStockCount = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                .count { it.quantity <= threshold }
            callback(lowStockCount)
        }.addOnFailureListener {
            callback(0)
        }
    }



    // Preload products into memory
    fun preloadProducts(callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = productDatabase.child(userId).get().await()
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
        val categoryId = categoryDatabase.push().key ?: return
        categoryDatabase.child(categoryId).setValue(mapOf("name" to categoryName))
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Remove a category
    fun removeCategory(categoryId: String, callback: (Boolean) -> Unit) {
        categoryDatabase.child(categoryId).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Fetch all categories
    fun getCategories(callback: (Map<String, String>) -> Unit) {
        categoryDatabase.get()
            .addOnSuccessListener { snapshot ->
                val categories = snapshot.children.associate { it.key!! to it.child("name").value.toString() }
                callback(categories)
            }.addOnFailureListener {
                callback(emptyMap())
            }
    }

    fun getRestockTrends(callback: (List<Product>) -> Unit) {
        productDatabase.get().addOnSuccessListener { snapshot ->
            val products = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                .sortedByDescending { it.quantitySold } // Sort by quantity sold (or other metrics)
            callback(products)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}