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
// UI STATE - Represents what the UI should show
// sealed class = Only these states exist (like enum but with data)
// ══════════════════════════════════════════════════════════════
sealed class LoginUiState {
    object Idle : LoginUiState()              // Initial state
    object Loading : LoginUiState()           // API call in progress
    data class Success(
        val authResponse: AuthResponse
    ) : LoginUiState()                        // Login successful
    data class Error(
        val message: String
    ) : LoginUiState()                        // Login failed
}

// ══════════════════════════════════════════════════════════════
// LOGIN VIEWMODEL
// Manages login screen logic and state
// ══════════════════════════════════════════════════════════════
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ════════════════════════════════════════════════════════
    // UI STATE FLOW
    //
    // MutableStateFlow = Can be changed (private)
    // StateFlow = Read-only version (public)
    //
    // UI observes this and updates automatically when it changes
    // ════════════════════════════════════════════════════════
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ════════════════════════════════════════════════════════
    // LOGIN FUNCTION
    // Called when user clicks "Login" button
    // ════════════════════════════════════════════════════════
    fun login(email: String, password: String) {
        // ═══ STEP 1: Validate input ═══
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Email and password cannot be empty")
            return
        }

        // ═══ STEP 2: Show loading state ═══
        _uiState.value = LoginUiState.Loading

        // ═══ STEP 3: Make API call (in background thread) ═══
        viewModelScope.launch {
            val result = authRepository.login(email, password)

            // ═══ STEP 4: Update UI based on result ═══
            _uiState.value = result.fold(
                onSuccess = { authResponse ->
                    LoginUiState.Success(authResponse)
                },
                onFailure = { exception ->
                    LoginUiState.Error(
                        exception.message ?: "Login failed. Please try again."
                    )
                }
            )
        }
    }

    // ════════════════════════════════════════════════════════
    // RESET STATE
    // Called to clear error messages
    // ════════════════════════════════════════════════════════
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    // ════════════════════════════════════════════════════════
    // CHECK IF ALREADY LOGGED IN
    // Called when app starts
    // ════════════════════════════════════════════════════════
    fun checkLoginStatus(onLoggedIn: () -> Unit, onNotLoggedIn: () -> Unit) {
        viewModelScope.launch {
            if (authRepository.isLoggedIn()) {
                onLoggedIn()
            } else {
                onNotLoggedIn()
            }
        }
    }
}