package com.felipeserri.userlistapp.model

data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val address: AddressResponse,
    val company: CompanyResponse
)

data class AddressResponse(
    val street: String,
    val city: String
)

data class CompanyResponse(
    val name: String
)