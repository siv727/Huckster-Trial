package com.android.huckster

data class User(val firstName: String, val lastName: String, val email: String, val password: String)

object UserData {
    private val users: ArrayList<User> = ArrayList()
    var loggedInUser: User? = null // Stores currently logged-in user

    fun registerUser(firstName: String, lastName: String, email: String, password: String): Boolean {
        if (users.any { it.email == email }) {
            return false // User already exists
        }
        val newUser = User(firstName, lastName, email, password)
        users.add(newUser)
        return true // Successfully registered
    }

    fun authenticate(email: String, password: String): Boolean {
        val user = users.find { it.email == email && it.password == password }
        loggedInUser = user // Store the logged-in user
        return user != null
    }

    fun getUsers(): List<User> = users.toList() // Returns a copy of the user list
}
