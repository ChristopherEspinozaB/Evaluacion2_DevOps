package com.example.supletanes.ui.screens.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.LoginRequest
import com.example.supletanes.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun login() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""

        viewModelScope.launch {
            val result = authRepository.login(LoginRequest(emailValue, passwordValue))

            result.onSuccess {
                _loginSuccess.value = true
                _error.value = null
            }

            result.onFailure { exception ->
                _loginSuccess.value = false
                _error.value = exception.message
            }
        }
    }
}