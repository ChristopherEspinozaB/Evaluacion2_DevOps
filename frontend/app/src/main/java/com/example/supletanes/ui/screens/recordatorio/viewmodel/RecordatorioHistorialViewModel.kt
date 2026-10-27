package com.example.supletanes.ui.screens.recordatorio.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.model.RecordatorioRequest
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID

data class RecordatorioHistorialUiState(
    val recordatorios: List<Recordatorio> = emptyList(),
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val showUndoAction: Boolean = false // Nuevo: saber si mostrar botón deshacer
)

@RequiresApi(Build.VERSION_CODES.O)
class RecordatorioHistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecordatorioHistorialUiState())
    val uiState: StateFlow<RecordatorioHistorialUiState> = _uiState.asStateFlow()

    // --- Dialog state management ---
    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    private val _showTimeDialog = MutableStateFlow(false)
    val showTimeDialog = _showTimeDialog.asStateFlow()

    // Dialog para Crear (Wizard)
    private val _showMensajeDialog = MutableStateFlow(false)
    val showMensajeDialog = _showMensajeDialog.asStateFlow()

    // Dialog para Editar (Nuevo)
    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog = _showEditDialog.asStateFlow()

    // Variables temporales
    private var _selectedDate: LocalDate? = null
    private var _selectedDateTime: LocalDateTime? = null
    
    // Para Edición
    private var _recordatorioEnEdicion: Recordatorio? = null
    val recordatorioEnEdicion: Recordatorio? get() = _recordatorioEnEdicion

    // Para Deshacer
    private var _ultimoRecordatorioEliminado: Recordatorio? = null

    init {
        obtenerRecordatorios()
    }

    // --- Logic for Creating (Wizard) ---
    fun onCalendarIconClick() { 
        _selectedDate = LocalDate.now() // Reset
        _showCalendarDialog.value = true 
    }
    
    fun onDismissCalendar() { _showCalendarDialog.value = false }
    fun onDismissTimeDialog() { _showTimeDialog.value = false }
    fun onDismissMensajeDialog() { _showMensajeDialog.value = false }
    
    // --- Logic for Editing ---
    fun onEditClick(recordatorio: Recordatorio) {
        _recordatorioEnEdicion = recordatorio
        _selectedDateTime = recordatorio.fechaHora
        _selectedDate = recordatorio.fechaHora.toLocalDate()
        _showEditDialog.value = true
    }
    
    fun onDismissEditDialog() { 
        _showEditDialog.value = false 
        _recordatorioEnEdicion = null
    }

    // Common date/time selection
    fun onDateSelected(dateInMillis: Long) {
        _selectedDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        _showCalendarDialog.value = false
        
        // Si estamos editando, volvemos al diálogo de edición, si no, seguimos el flujo de creación
        if (_recordatorioEnEdicion != null) {
            // Actualizamos la fecha temporal manteniendo la hora
            val horaActual = _selectedDateTime?.toLocalTime() ?: LocalTime.now()
             _selectedDate?.let {
                _selectedDateTime = LocalDateTime.of(it, horaActual)
            }
            _showEditDialog.value = true
        } else {
            _showTimeDialog.value = true
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _selectedDate?.let {
            _selectedDateTime = LocalDateTime.of(it, LocalTime.of(hour, minute))
        }
        _showTimeDialog.value = false
        
        // Si estamos editando, volvemos al diálogo de edición, si no, seguimos al mensaje
        if (_recordatorioEnEdicion != null) {
            _showEditDialog.value = true
        } else {
            _showMensajeDialog.value = true
        }
    }
    
    // Helpers para abrir pickers desde Edición
    fun onChangeDateClick() {
        _showEditDialog.value = false // Ocultamos temporalmente el de edición
        _showCalendarDialog.value = true
    }
    
    fun onChangeTimeClick() {
        _showEditDialog.value = false // Ocultamos temporalmente el de edición
        _showTimeDialog.value = true
    }

    private fun getDeviceUserId(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getString("user_id", null)
        if (userId == null) {
            userId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("user_id", userId).apply()
        }
        return userId
    }

    fun obtenerRecordatorios() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = ApiClient.recordatorioApiService.obtenerRecordatoriosPorUsuario(getDeviceUserId())
                if (response.isSuccessful) {
                    _uiState.update { it.copy(recordatorios = response.body() ?: emptyList(), isLoading = false) }
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al obtener recordatorios", isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al obtener recordatorios", e)
                _uiState.update { it.copy(snackbarMessage = e.message, isLoading = false) }
            }
        }
    }

    fun crearRecordatorio(mensaje: String) = viewModelScope.launch {
        _showMensajeDialog.value = false
        _selectedDateTime?.let { fechaHora ->
            _uiState.update { it.copy(isLoading = true) }
            val request = RecordatorioRequest(
                idUsuario = getDeviceUserId(),
                mensaje = mensaje,
                fechaHora = fechaHora
            )
            try {
                val response = ApiClient.recordatorioApiService.crearRecordatorio(request)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(snackbarMessage = "Recordatorio creado") }
                    obtenerRecordatorios()
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al crear recordatorio") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(snackbarMessage = e.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun guardarEdicion(nuevoMensaje: String) = viewModelScope.launch {
        val recordatorioOriginal = _recordatorioEnEdicion ?: return@launch
        _showEditDialog.value = false
        
        val fechaFinal = _selectedDateTime ?: recordatorioOriginal.fechaHora
        
        val recordatorioActualizado = recordatorioOriginal.copy(
            mensaje = nuevoMensaje,
            fechaHora = fechaFinal
        )

        _uiState.update { it.copy(isLoading = true) }
        try {
            val response = ApiClient.recordatorioApiService.actualizarRecordatorio(recordatorioOriginal.id, recordatorioActualizado)
            if (response.isSuccessful) {
                _uiState.update { it.copy(snackbarMessage = "Recordatorio actualizado") }
                obtenerRecordatorios()
            } else {
                _uiState.update { it.copy(snackbarMessage = "Error al actualizar recordatorio") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(snackbarMessage = e.message) }
        }
        _uiState.update { it.copy(isLoading = false) }
        _recordatorioEnEdicion = null
    }

    fun eliminarRecordatorio(recordatorio: Recordatorio) {
        _ultimoRecordatorioEliminado = recordatorio // Guardar para deshacer
        viewModelScope.launch {
            try {
                val response = ApiClient.recordatorioApiService.eliminarRecordatorio(recordatorio.id)
                if (response.isSuccessful) {
                    // Notificamos éxito Y activamos el flag de deshacer
                    _uiState.update { it.copy(
                        snackbarMessage = "Recordatorio eliminado", 
                        showUndoAction = true 
                    )}
                    obtenerRecordatorios()
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al eliminar el recordatorio", showUndoAction = false) }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al eliminar recordatorio", e)
                _uiState.update { it.copy(snackbarMessage = e.message, showUndoAction = false) }
            }
        }
    }

    fun deshacerEliminacion() {
        _ultimoRecordatorioEliminado?.let { recordatorio ->
             viewModelScope.launch {
                 // Creamos uno nuevo con los mismos datos
                 val request = RecordatorioRequest(
                    idUsuario = recordatorio.idUsuario,
                    mensaje = recordatorio.mensaje,
                    fechaHora = recordatorio.fechaHora
                )
                try {
                    val response = ApiClient.recordatorioApiService.crearRecordatorio(request)
                    if (response.isSuccessful) {
                        _uiState.update { it.copy(snackbarMessage = "Recordatorio restaurado", showUndoAction = false) }
                        obtenerRecordatorios()
                    } 
                } catch (e: Exception) {
                    _uiState.update { it.copy(snackbarMessage = "No se pudo restaurar: ${e.message}") }
                }
             }
        }
        _ultimoRecordatorioEliminado = null
    }

    fun limpiarMensaje() {
        _uiState.update { it.copy(snackbarMessage = null, showUndoAction = false) }
    }
    
    // Helper para la UI saber qué fecha mostrar en el diálogo de edición
    fun getFechaHoraEdicion(): LocalDateTime? {
        return _selectedDateTime
    }
}
