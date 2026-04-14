package com.felipeserri.userlistapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felipeserri.userlistapp.model.UiState
import com.felipeserri.userlistapp.repository.UserRepository
import com.felipeserri.userlistapp.utils.NetworkUtils
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState
    fun fetchUsers(context: Context) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (!NetworkUtils.isNetworkAvailable(context)) {
                _uiState.value = UiState.Error(
                    "Sem conexão com a internet.\nVerifique seu WiFi ou dados móveis."
                )
                return@launch
            }
            val result = repository.getUsers()

            if (result.isSuccess) {
                val users = result.getOrNull() ?: emptyList()
                _uiState.value = UiState.Success(users)
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                    ?: "Erro desconhecido"
                _uiState.value = UiState.Error(errorMessage)
            }
        }
    }
}