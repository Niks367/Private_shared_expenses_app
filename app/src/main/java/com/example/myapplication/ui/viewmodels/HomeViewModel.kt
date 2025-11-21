package com.example.myapplication.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.RetrofitClient
import com.example.myapplication.interfaces.UserBalance
import com.example.myapplication.model.TransactionDto
import com.example.myapplication.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userBalance: UserBalance? = null
)
class HomeViewModel( private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            repository.getUserBalance(userId)
                .onStart { updateLoading(true) }
                .catch { throwable ->
                    updateError(throwable.localizedMessage
                        ?: "Unexpected error")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { userBalance ->
                            _uiState.value = UiState(
                                isLoading = false,
                                error = null,
                                userBalance = userBalance
                            )
                        },
                        onFailure = { err ->
                            updateError(err.localizedMessage
                                ?: "Failed to load data")
                        }
                    )
                }
        }
    }
    private fun updateLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading, error = null)
    }
    private fun updateError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = message
        )
    }
    fun fetchUserBalance(userId: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                val response = RetrofitClient.apiService.getUserBalance(userId)
                if (response.isSuccessful) {
                    val userBalance = response.body()?.firstOrNull { it.id == userId }?.balance
                    _uiState.value = UiState(
                        isLoading = false,
                        userBalance = userBalance as UserBalance?
                    )
                } else {
                    _uiState.value = UiState(
                        isLoading = false,
                        error = "Failed to fetch balance: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    error = "Failed to fetch balance: ${e.message}"
                )
            }
        }
    }
}
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val user: UserBalance) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
