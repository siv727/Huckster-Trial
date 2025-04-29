package com.android.huckster.utils

import com.google.firebase.database.FirebaseDatabase

data class Product(
    val productName: String = "",
    var price: Double = 0.0,
    var unit: String = "",
    var quantity: Int = 0,
    var category: String = ""
)

object ProductData {
    private val database = FirebaseDatabase.getInstance().getReference("Products")

    // Add a new product to Firebase
    fun addProduct(
        productName: String,
        unit: String,
        price: Double,
        quantity: Int,
        category: String,
        callback: (Boolean) -> Unit
    ) {
        val product = Product(productName, price, unit, quantity, category)
        database.child(productName).setValue(product)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    // Update an existing product in Firebase
    fun updateProduct(
        productName: String,
        newUnit: String,
        newPrice: Double,
        newQuantity: Int,
        newCategory: String,
        callback: (Boolean) -> Unit
    ) {
        val updates = mapOf(
            "unit" to newUnit,
            "price" to newPrice,
            "quantity" to newQuantity,
            "category" to newCategory
        )
        database.child(productName).updateChildren(updates)
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
    fun getLowStockProducts(threshold: Int, callback: (List<Product>) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            val lowStockProducts = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                .filter { it.quantity <= threshold }
            callback(lowStockProducts)
        }.addOnFailureListener {
            callback(emptyList())
        }
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
}