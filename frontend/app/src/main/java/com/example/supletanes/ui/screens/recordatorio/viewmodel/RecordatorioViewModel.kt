package com.example.supletanes.ui.screens.recordatorio.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.model.RecordatorioRequest
import com.example.supletanes.data.repository.RecordatorioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class RecordatorioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecordatorioRepository()

    // Lista de recordatorios
    private val _allRecordatorios = MutableStateFlow<List<Recordatorio>>(emptyList())
    val allRecordatorios: StateFlow<List<Recordatorio>> = _allRecordatorios.asStateFlow()

    // Estados de carga y mensajes
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensajeSnackbar = MutableStateFlow<String?>(null)
    val mensajeSnackbar: StateFlow<String?> = _mensajeSnackbar.asStateFlow()

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _isLoading.update { true }
            repository.obtenerRecordatorios()
                .onSuccess { lista ->
                    _allRecordatorios.update { lista }
                    Log.d("RecordatorioViewModel", "${lista.size} recordatorios cargados")
                }
                .onFailure { error ->
                    Log.e("RecordatorioViewModel", "Error al cargar recordatorios", error)
                    _mensajeSnackbar.update {
                        if (error is java.net.SocketTimeoutException) {
                            "Timeout al cargar, el servidor puede estar despertando"
                        } else {
                            "Error al cargar recordatorios"
                        }
                    }
                }
            _isLoading.update { false }
        }
    }

    // Lógica para obtener/crear un ID de dispositivo único
    private fun getDeviceUserId(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getString("user_id", null)
        if (userId == null) {
            userId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("user_id", userId).apply()
        }
        return userId
    }

    fun crearRecordatorio(mensaje: String, fechaHora: LocalDateTime) = viewModelScope.launch {
        _isLoading.update { true }
        val request = RecordatorioRequest(
            idUsuario = getDeviceUserId(),
            mensaje = mensaje,
            fechaHora = fechaHora
        )
        repository.crearRecordatorio(request)
            .onSuccess {
                Log.d("RecordatorioViewModel", "Recordatorio creado")
                _mensajeSnackbar.update { "Recordatorio creado exitosamente" }
                cargarRecordatorios()
            }
            .onFailure { error ->
                Log.e("RecordatorioViewModel", "Error al crear", error)
                _mensajeSnackbar.update { "Error al crear recordatorio" }
            }
        _isLoading.update { false }
    }

    fun actualizarRecordatorio(recordatorio: Recordatorio) = viewModelScope.launch {
        _isLoading.update { true }
        repository.actualizarRecordatorio(recordatorio.id, recordatorio)
            .onSuccess {
                Log.d("RecordatorioViewModel", "Recordatorio actualizado")
                _mensajeSnackbar.update { "Recordatorio actualizado" }
                cargarRecordatorios()
            }
            .onFailure { error ->
                Log.e("RecordatorioViewModel", "Error al actualizar", error)
                _mensajeSnackbar.update { "Error al actualizar recordatorio" }
            }
        _isLoading.update { false }
    }

    fun eliminarRecordatorio(id: Long) = viewModelScope.launch {
        _isLoading.update { true }
        repository.eliminarRecordatorio(id)
            .onSuccess {
                Log.d("RecordatorioViewModel", "Recordatorio eliminado")
                _mensajeSnackbar.update { "Recordatorio eliminado" }
                cargarRecordatorios()
            }
            .onFailure { error ->
                Log.e("RecordatorioViewModel", "Error al eliminar", error)
                _mensajeSnackbar.update { "Error al eliminar recordatorio" }
            }
        _isLoading.update { false }
    }

    fun limpiarMensaje() {
        _mensajeSnackbar.update { null }
    }

    fun refrescar() {
        cargarRecordatorios()
    }
}