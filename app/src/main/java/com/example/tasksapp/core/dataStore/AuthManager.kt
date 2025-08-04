package com.example.tasksapp.core.dataStore

object AuthManager {
    private var authToken: String? = null
    private var userId: Int? = null

    fun setToken(token: String) {
        authToken = token
    }

    fun setUserId(id: Int) {
        userId = id
    }

    fun getToken(): String? {
        return authToken
    }

    fun getUserId(): Int? {
        return userId
    }

    fun clearToken() {
        authToken = null
        userId = null
    }

    fun isLoggedIn(): Boolean {
        return !authToken.isNullOrEmpty()
    }
}