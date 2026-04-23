package com.felipeserri.userlistapp.repository

import com.felipeserri.userlistapp.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}