package com.felipeserri.userlistapp.model

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val address: Address,
    val company: Company
)

data class Address(
    val street: String,
    val city: String
)

data class Company(
    val name: String
)