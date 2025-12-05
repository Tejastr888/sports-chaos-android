package com.shk.sportschaos.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// ══════════════════════════════════════════════════════════════
// RETROFIT CLIENT - Singleton that creates API instances
// ══════════════════════════════════════════════════════════════
object RetrofitClient {

    // ════════════════════════════════════════════════════════
    // BASE URL - Your backend address
    //
    // IMPORTANT:
    // - Emulator: use "http://10.0.2.2:8080/"
    // - Physical device: use "http://YOUR_COMPUTER_IP:8080/"
    //   Find your IP: Open CMD → type "ipconfig" → look for IPv4
    //   Example: "http://192.168.1.5:8080/"
    // ════════════════════════════════════════════════════════
//    private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val BASE_URL = "https://auth-service-production-0c22.up.railway.app/"


    // ════════════════════════════════════════════════════════
    // LOGGING INTERCEPTOR - Shows API calls in Logcat
    // LEVEL.BODY = Shows full request/response (helpful for debugging)
    // ════════════════════════════════════════════════════════
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ════════════════════════════════════════════════════════
    // OKHTTP CLIENT - Handles actual HTTP connections
    // ════════════════════════════════════════════════════════
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // Add logging
        .connectTimeout(30, TimeUnit.SECONDS) // Wait 30s to connect
        .readTimeout(30, TimeUnit.SECONDS)    // Wait 30s to read response
        .writeTimeout(30, TimeUnit.SECONDS)   // Wait 30s to send request
        .build()

    // ════════════════════════════════════════════════════════
    // RETROFIT INSTANCE - Converts API interface to real implementation
    // ════════════════════════════════════════════════════════
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)                                // Your backend URL
        .client(okHttpClient)                             // Use our configured OkHttp
        .addConverterFactory(GsonConverterFactory.create()) // JSON ↔ Kotlin objects
        .build()

    // ════════════════════════════════════════════════════════
    // API INSTANCE - Lazy-created singleton
    // Only creates AuthApi when first accessed
    // ════════════════════════════════════════════════════════
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
}