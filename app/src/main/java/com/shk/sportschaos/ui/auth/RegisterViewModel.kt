package com.shk.sportschaos.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shk.sportschaos.data.model.AuthResponse
import com.shk.sportschaos.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ══════════════════════════════════════════════════════════════
// UI STATE for Register Screen
// ══════════════════════════════════════════════════════════════
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val authResponse: AuthResponse) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

// ══════════════════════════════════════════════════════════════
// REGISTER VIEWMODEL
// ══════════════════════════════════════════════════════════════
class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String?
    ) {
        // ═══ Validation ═══
        when {
            name.isBlank() -> {
                _uiState.value = RegisterUiState.Error("Name cannot be empty")
                return
            }
            email.isBlank() -> {
                _uiState.value = RegisterUiState.Error("Email cannot be empty")
                return
            }
            password.isBlank() -> {
                _uiState.value = RegisterUiState.Error("Password cannot be empty")
                return
            }
            password.length < 6 -> {
                _uiState.value = RegisterUiState.Error("Password must be at least 6 characters")
                return
            }
            password != confirmPassword -> {
                _uiState.value = RegisterUiState.Error("Passwords do not match")
                return
            }
        }

        // ═══ Show loading ═══
        _uiState.value = RegisterUiState.Loading

        // ═══ Make API call ═══
        viewModelScope.launch {
            val result = authRepository.register(name, email, password, phoneNumber)

            _uiState.value = result.fold(
                onSuccess = { authResponse ->
                    RegisterUiState.Success(authResponse)
                },
                onFailure = { exception ->
                    RegisterUiState.Error(
                        exception.message ?: "Registration failed. Please try again."
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}