package com.felipeserri.userlistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felipeserri.userlistapp.model.UiState
import com.felipeserri.userlistapp.repository.UserRepository
import com.felipeserri.userlistapp.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    // ✅ fetchUsers sem Context — ViewModel agnóstico de framework
    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = repository.getUsers()

            _uiState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(
                    result.exceptionOrNull()?.message ?: "Erro desconhecido"
                )
            }
        }
    }
}