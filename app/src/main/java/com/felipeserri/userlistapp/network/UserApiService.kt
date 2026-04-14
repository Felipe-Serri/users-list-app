package com.felipeserri.userlistapp.network

import com.felipeserri.userlistapp.model.User
import retrofit2.http.GET

interface UserApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

}