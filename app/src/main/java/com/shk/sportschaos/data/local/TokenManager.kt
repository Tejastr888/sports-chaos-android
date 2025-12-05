package com.shk.sportschaos.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ══════════════════════════════════════════════════════════════
// Creates a DataStore instance (like a local database)
// Stores key-value pairs persistently
// ══════════════════════════════════════════════════════════════
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_prefs"
)

class TokenManager(private val context: Context) {

    // ════════════════════════════════════════════════════════
    // KEYS - Used to store/retrieve data
    // Like column names in a database
    // ════════════════════════════════════════════════════════
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    // ════════════════════════════════════════════════════════
    // SAVE TOKEN - Called after successful login
    // suspend = must be called from coroutine (async)
    // ════════════════════════════════════════════════════════
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // ════════════════════════════════════════════════════════
    // SAVE USER INFO - Store user details locally
    // ════════════════════════════════════════════════════════
    suspend fun saveUserInfo(
        userId: Long,
        email: String,
        name: String,
        role: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId.toString()
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_NAME_KEY] = name
            preferences[USER_ROLE_KEY] = role
        }
    }

    // ════════════════════════════════════════════════════════
    // GET TOKEN - Returns Flow (live data stream)
    // Flow emits new values when data changes
    // ════════════════════════════════════════════════════════
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    // ════════════════════════════════════════════════════════
    // GET USER INFO - Get all saved user data
    // ════════════════════════════════════════════════════════
    fun getUserInfo(): Flow<UserInfo?> {
        return context.dataStore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY]?.toLongOrNull()
            val email = preferences[USER_EMAIL_KEY]
            val name = preferences[USER_NAME_KEY]
            val role = preferences[USER_ROLE_KEY]

            if (userId != null && email != null && name != null) {
                UserInfo(userId, email, name, role ?: "USER")
            } else {
                null
            }
        }
    }

    // ════════════════════════════════════════════════════════
    // CLEAR ALL - Logout functionality
    // ════════════════════════════════════════════════════════
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}

// Helper data class
data class UserInfo(
    val userId: Long,
    val email: String,
    val name: String,
    val role: String
)