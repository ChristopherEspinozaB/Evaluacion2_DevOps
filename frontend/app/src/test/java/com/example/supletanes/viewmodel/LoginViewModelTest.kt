package com.example.supletanes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.supletanes.data.model.LoginRequest
import com.example.supletanes.data.model.LoginResponse
import com.example.supletanes.data.model.User
import com.example.supletanes.data.repository.AuthRepository
import com.example.supletanes.ui.screens.auth.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        loginViewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `dado email y password correctos cuando se llama a login entonces loginSuccess debe ser true`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val mockUser = User(id = 1, email = email, name = "Test User", role = "USER")
        val mockResponse = LoginResponse(token = "fake-jwt-token", user = mockUser)

        coEvery { authRepository.login(LoginRequest(email, password)) } returns Result.success(mockResponse)

        loginViewModel.email.value = email
        loginViewModel.password.value = password
        loginViewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, loginViewModel.loginSuccess.value)
        assertNull(loginViewModel.error.value)
    }

    @Test
    fun `dado email y password incorrectos cuando se llama a login entonces error debe contener mensaje`() = runTest {
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val errorMessage = "Credenciales inválidas"

        coEvery { authRepository.login(LoginRequest(email, password)) } returns Result.failure(Exception(errorMessage))

        loginViewModel.email.value = email
        loginViewModel.password.value = password
        loginViewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, loginViewModel.loginSuccess.value)
        assertEquals(errorMessage, loginViewModel.error.value)
    }
}