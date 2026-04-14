package com.felipeserri.userlistapp.model

sealed class UiState {

    object Loading : UiState()
    data class Success(val users: List<User>) : UiState()
    data class Error(val message: String) : UiState()
}