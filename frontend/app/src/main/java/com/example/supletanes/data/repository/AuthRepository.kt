package com.example.supletanes.data.repository

import com.example.supletanes.data.model.LoginRequest
import com.example.supletanes.data.model.LoginResponse
import com.example.supletanes.data.network.AuthService

class AuthRepository(private val authService: AuthService) {

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = authService.login(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}