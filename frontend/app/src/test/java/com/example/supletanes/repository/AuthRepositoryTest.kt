package com.example.supletanes.repository

import com.example.supletanes.data.model.LoginRequest
import com.example.supletanes.data.model.LoginResponse
import com.example.supletanes.data.model.User
import com.example.supletanes.data.network.AuthService
import com.example.supletanes.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private lateinit var authService: AuthService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authService = mockk()
        authRepository = AuthRepository(authService)
    }

    @Test
    fun `login deberia devolver success cuando la API responde exitosamente`() = runTest {
        val loginRequest = LoginRequest("test@example.com", "password")
        val mockUser = User(1, "test@example.com", "Test User", "USER")
        val mockResponse = LoginResponse("fake-token", mockUser)

        coEvery { authService.login(loginRequest) } returns Response.success(mockResponse)

        val result = authRepository.login(loginRequest)

        assertTrue(result.isSuccess)
        assertEquals("fake-token", result.getOrNull()?.token)
        assertEquals("test@example.com", result.getOrNull()?.user?.email)
    }

    @Test
    fun `login deberia devolver failure cuando la API responde con error`() = runTest {
        val loginRequest = LoginRequest("wrong@example.com", "wrongpassword")
        val errorJson = """{"message":"Unauthorized"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { authService.login(loginRequest) } returns Response.error(401, errorBody)

        val result = authRepository.login(loginRequest)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.startsWith("Login failed with code 401") == true)
    }

    @Test
    fun `login deberia devolver failure cuando la API lanza excepcion`() = runTest {
        val loginRequest = LoginRequest("net@error.com", "password")
        val networkException = Exception("Network error")

        coEvery { authService.login(loginRequest) } throws networkException

        val result = authRepository.login(loginRequest)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}