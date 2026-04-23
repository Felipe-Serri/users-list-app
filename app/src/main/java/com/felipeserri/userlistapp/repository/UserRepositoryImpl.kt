package com.felipeserri.userlistapp.repository

import android.content.Context
import com.felipeserri.userlistapp.model.User
import com.felipeserri.userlistapp.model.UserResponse
import com.felipeserri.userlistapp.network.UserRemoteDataSource
import com.felipeserri.userlistapp.utils.NetworkUtils

class UserRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return Result.failure(
                Exception("Sem conexão com a internet.\nVerifique seu WiFi ou dados móveis.")
            )
        }

        return try {
            val response = remoteDataSource.fetchUsers()
            // ✅ Converte UserResponse → User (modelo de domínio)
            val users = response.map { it.toDomain() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    private fun UserResponse.toDomain() = User(
        id       = id,
        name     = name,
        username = username,
        email    = email,
        phone    = phone,
        website  = website,
        address  = "${address.street}, ${address.city}",
        company  = company.name
    )
}