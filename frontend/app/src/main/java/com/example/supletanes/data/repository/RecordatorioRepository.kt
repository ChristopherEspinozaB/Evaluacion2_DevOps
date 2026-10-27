package com.example.supletanes.data.repository

import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.model.RecordatorioRequest
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecordatorioRepository {

    private val apiService = ApiClient.recordatorioApiService

    suspend fun obtenerRecordatorios(): Result<List<Recordatorio>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.obtenerRecordatorios()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener recordatorios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearRecordatorio(request: RecordatorioRequest): Result<Recordatorio> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.crearRecordatorio(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al crear recordatorio: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun actualizarRecordatorio(id: Long, recordatorio: Recordatorio): Result<Recordatorio> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.actualizarRecordatorio(id, recordatorio)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al actualizar recordatorio: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun eliminarRecordatorio(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.eliminarRecordatorio(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar recordatorio: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}