package com.example.myapplication.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.RetrofitClient
import com.example.myapplication.model.BalanceDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    data class UiState(
        val isLoading: Boolean = true,
        val error: String? = null,
        val balance: BalanceDto? = null
    )

    fun fetchUserBalance(userId: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                val response = RetrofitClient.apiService.getUserBalance(userId)
                if (response.isSuccessful) {
                    val userBalance = response.body()?.firstOrNull { it.id == userId }?.balance
                    _uiState.value = UiState(
                        isLoading = false,
                        balance = userBalance
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
