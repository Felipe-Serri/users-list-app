package com.felipeserri.userlistapp.network

import com.felipeserri.userlistapp.model.UserResponse

class UserRemoteDataSource(
    private val api: UserApiService = RetrofitInstance.api
) {
    suspend fun fetchUsers(): List<UserResponse> {
        return api.getUsers()
    }
}