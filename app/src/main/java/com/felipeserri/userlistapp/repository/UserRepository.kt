package com.felipeserri.userlistapp.repository

import com.felipeserri.userlistapp.model.User
import com.felipeserri.userlistapp.network.RetrofitInstance


class UserRepository {
    private val api = RetrofitInstance.api
    suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = api.getUsers()
            Result.success(users)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}