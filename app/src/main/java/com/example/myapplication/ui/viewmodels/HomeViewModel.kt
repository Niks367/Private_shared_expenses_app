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
        // Skip API call for numeric user IDs (new users from database)
        // API only has string IDs like "alice", "bob"
        if (userId.toIntOrNull() != null) {
            // New user - show empty state without API call
            _uiState.value = UiState(
                isLoading = false,
                error = null,
                userBalance = null
            )
            return
        }
        
        viewModelScope.launch {
            repository.getUserBalance(userId)
                .onStart { updateLoading(true) }
                .catch { throwable ->
                    // Network error - don't show error for new users
                    _uiState.value = UiState(
                        isLoading = false,
                        error = null,  // Hide error, show empty state instead
                        userBalance = null
                    )
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
                            // User not found in API - normal for new users
                            _uiState.value = UiState(
                                isLoading = false,
                                error = null,  // Don't show error
                                userBalance = null  // Show empty balance
                            )
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
        // Skip API call for numeric user IDs (new users from database)
        if (userId.toIntOrNull() != null) {
            _uiState.value = UiState(
                isLoading = false,
                error = null,
                userBalance = null
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                val response = RetrofitClient.apiService.getUserBalance(userId)
                if (response.isSuccessful) {
                    val userBalance = response.body()?.firstOrNull { it.id == userId }?.balance
                    if (userBalance != null) {
                        _uiState.value = UiState(
                            isLoading = false,
                            userBalance = userBalance as UserBalance?
                        )
                    } else {
                        // User not found in API - show empty state for new users
                        _uiState.value = UiState(
                            isLoading = false,
                            userBalance = null,
                            error = null  // No error, just empty state
                        )
                    }
                } else {
                    _uiState.value = UiState(
                        isLoading = false,
                        error = "Failed to fetch balance: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // Network error - show error message with retry option
                _uiState.value = UiState(
                    isLoading = false,
                    error = "Unable to connect to server. Please check your connection."
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
