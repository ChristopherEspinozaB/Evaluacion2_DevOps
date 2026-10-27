package com.example.supletanes.ui.screens.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    // 1. Canal para enviar eventos de UI (como navegación)
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Estados de los campos
    var currentPassword by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    // Estados de los errores
    var currentPasswordError by mutableStateOf<String?>(null)
        private set
    var newPasswordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    // Funciones 'on-change'
    fun onCurrentPasswordChange(pass: String) { currentPassword = pass }
    fun onNewPasswordChange(pass: String) { newPassword = pass }
    fun onConfirmPasswordChange(pass: String) { confirmPassword = pass }

    private fun validate(): Boolean {
        // Limpiamos errores previos para re-validar
        currentPasswordError = if (currentPassword.isBlank()) "La contraseña actual es requerida" else null
        newPasswordError = if (newPassword.length < 6) "Debe tener al menos 6 caracteres" else null
        confirmPasswordError = if (newPassword != confirmPassword) "Las contraseñas no coinciden" else null

        // Devuelve true si no hay ningún error
        return currentPasswordError == null && newPasswordError == null && confirmPasswordError == null
    }

    fun onSaveClick() {
        if (validate()) {
            viewModelScope.launch {
                // Lógica para cambiar la contraseña en el servidor...
                println("Contraseña cambiada exitosamente")

                // 2. Enviar el evento para navegar hacia atrás
                _uiEvent.send(UiEvent.NavigateBack)
            }
        }
    }

    // Clase sellada para definir todos los posibles eventos de UI
    sealed class UiEvent {
        object NavigateBack : UiEvent()
        // Podrías añadir más, como: data class ShowSnackbar(val message: String) : UiEvent()
    }
}