package com.shk.sportschaos.data.api

import com.shk.sportschaos.data.model.ApiResponse
import com.shk.sportschaos.data.model.AuthResponse
import com.shk.sportschaos.data.model.LoginRequest
import com.shk.sportschaos.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// ══════════════════════════════════════════════════════════════
// API INTERFACE - Defines all auth endpoints
// Retrofit converts these to actual HTTP calls
// ══════════════════════════════════════════════════════════════
interface AuthApi {

    // ════════════════════════════════════════════════════════
    // POST /api/auth/login
    // Sends LoginRequest, receives AuthResponse wrapped in ApiResponse
    // suspend = can be called from coroutine (async function)
    // ════════════════════════════════════════════════════════
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest  // Converts Kotlin object to JSON
    ): Response<ApiResponse<AuthResponse>>

    // ════════════════════════════════════════════════════════
    // POST /api/auth/register
    // ════════════════════════════════════════════════════════
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>

    // ════════════════════════════════════════════════════════
    // GET /api/auth/validate
    // Checks if JWT token is still valid
    // @Header adds "Authorization: Bearer <token>" to request
    // ════════════════════════════════════════════════════════
    @GET("api/auth/validate")
    suspend fun validateToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AuthResponse>>
}