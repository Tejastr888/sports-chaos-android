package com.shk.sportschaos.data.repository

import com.shk.sportschaos.data.api.RetrofitClient
import com.shk.sportschaos.data.local.TokenManager
import com.shk.sportschaos.data.model.AuthResponse
import com.shk.sportschaos.data.model.LoginRequest
import com.shk.sportschaos.data.model.RegisterRequest
import kotlinx.coroutines.flow.first

// ══════════════════════════════════════════════════════════════
// REPOSITORY - Handles data operations
// Why? Separates business logic from UI
// UI doesn't need to know HOW data is fetched, just THAT it is
// ══════════════════════════════════════════════════════════════
class AuthRepository(private val tokenManager: TokenManager) {

    // Get the API instance from RetrofitClient
    private val authApi = RetrofitClient.authApi

    // ════════════════════════════════════════════════════════
    // LOGIN FUNCTION
    //
    // What happens:
    // 1. Call backend API with email/password
    // 2. If success → Save token & user info locally
    // 3. Return Result (Success or Failure)
    //
    // Result<T> = Either success with data T, or failure with error
    // ════════════════════════════════════════════════════════
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            // ═══ STEP 1: Make API call ═══
            val response = authApi.login(LoginRequest(email, password))

            // ═══ STEP 2: Check if successful ═══
            if (response.isSuccessful && response.body()?.data != null) {
                val authResponse = response.body()!!.data!!

                // ═══ STEP 3: Save token & user info ═══
                tokenManager.saveToken(authResponse.token)
                tokenManager.saveUserInfo(
                    authResponse.userId,
                    authResponse.email,
                    authResponse.name,
                    authResponse.role
                )

                // ═══ STEP 4: Return success ═══
                Result.success(authResponse)
            } else {
                // API call succeeded but returned error message
                val errorMessage = response.body()?.message
                    ?: response.message()
                    ?: "Login failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            // Network error, timeout, or other exception
            Result.failure(e)
        }
    }

    // ════════════════════════════════════════════════════════
    // REGISTER FUNCTION
    // Similar to login but calls register endpoint
    // ════════════════════════════════════════════════════════
    suspend fun register(
        name: String,
        email: String,
        password: String,
        phoneNumber: String?
    ): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(name, email, password, phoneNumber)
            val response = authApi.register(request)

            if (response.isSuccessful && response.body()?.data != null) {
                val authResponse = response.body()!!.data!!

                tokenManager.saveToken(authResponse.token)
                tokenManager.saveUserInfo(
                    authResponse.userId,
                    authResponse.email,
                    authResponse.name,
                    authResponse.role
                )

                Result.success(authResponse)
            } else {
                val errorMessage = response.body()?.message
                    ?: "Registration failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ════════════════════════════════════════════════════════
    // CHECK IF USER IS LOGGED IN
    // Reads token from DataStore
    // .first() gets the current value from Flow
    // ════════════════════════════════════════════════════════
    suspend fun isLoggedIn(): Boolean {
        val token = tokenManager.getToken().first()
        return !token.isNullOrEmpty()
    }

    // ════════════════════════════════════════════════════════
    // LOGOUT
    // Clears all saved data
    // ════════════════════════════════════════════════════════
    suspend fun logout() {
        tokenManager.clearAll()
    }

    // ════════════════════════════════════════════════════════
    // GET SAVED TOKEN
    // Used later for authenticated API calls
    // ════════════════════════════════════════════════════════
    suspend fun getToken(): String? {
        return tokenManager.getToken().first()
    }
}