package com.shk.sportschaos.data.model

// ══════════════════════════════════════════════════════════════
// LOGIN REQUEST - What we send to backend
// Matches your Spring Boot LoginRequest.java
// ══════════════════════════════════════════════════════════════
data class LoginRequest(
        val email: String,
        val password: String
)

// ══════════════════════════════════════════════════════════════
// REGISTER REQUEST - What we send for registration
// Matches your Spring Boot RegisterRequest.java
// ══════════════════════════════════════════════════════════════
data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String,
        val phoneNumber: String? = null,  // Optional field
        val role: String = "USER"          // Default value
)

// ══════════════════════════════════════════════════════════════
// AUTH RESPONSE - What backend sends back
// Matches your Spring Boot AuthResponse.java
// ══════════════════════════════════════════════════════════════
data class AuthResponse(
        val token: String,      // JWT token
        val userId: Long,       // User ID
        val email: String,      // User email
        val name: String,       // User name
        val role: String        // USER, ADMIN, etc.
)

// ══════════════════════════════════════════════════════════════
// API WRAPPER - Standard response format from your backend
// Wraps the actual data with status and message
// ══════════════════════════════════════════════════════════════
data class ApiResponse<T>(
        val status: Int,
        val message: String?,
        val data: T?
)