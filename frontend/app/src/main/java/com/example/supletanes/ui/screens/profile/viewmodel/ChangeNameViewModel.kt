package com.example.supletanes.ui.screens.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChangeNameViewModel : ViewModel() {

    // --- INICIO DE LA REPARACIÓN ---

    // 1. Canal para enviar eventos de una sola vez (como la navegación o un snackbar)
    private val _uiEvent = Channel<UiEvent>()
    // 2. Propiedad pública 'uiEvent' que la pantalla puede observar. ESTO es lo que faltaba.
    val uiEvent = _uiEvent.receiveAsFlow()

    // --- FIN DE LA REPARACIÓN ---

    var name by mutableStateOf("")
        private set
    var nameError by mutableStateOf<String?>(null)
        private set

    fun onNameChange(newName: String) {
        name = newName
        if (nameError != null) {
            validateName()
        }
    }

    private fun validateName(): Boolean {
        nameError = if (name.isBlank()) {
            "El nombre no puede estar vacío"
        } else if (name.length < 3) {
            "El nombre debe tener al menos 3 caracteres"
        } else {
            null // No hay error
        }
        return nameError == null
    }

    fun onSaveClick() {
        if (validateName()) {
            // Se ejecuta en un hilo secundario para no bloquear la UI
            viewModelScope.launch {
                // Lógica para guardar el nombre en el servidor...
                println("Nombre guardado: $name")

                // 3. Enviar el evento para navegar hacia atrás
                _uiEvent.send(UiEvent.NavigateBack)
            }
        }
    }

    // Clase sellada para definir todos los posibles eventos de UI que este ViewModel puede emitir
    sealed class UiEvent {
        object NavigateBack : UiEvent()
    }
}