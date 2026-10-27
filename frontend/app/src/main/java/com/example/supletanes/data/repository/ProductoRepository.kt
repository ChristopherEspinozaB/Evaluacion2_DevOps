package com.example.supletanes.data.repository

import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository {

    private val apiService = ApiClient.productoApiService

    suspend fun obtenerProductos(): Result<List<Producto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.obtenerProductos()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerProductoPorId(id: Long): Result<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.obtenerProductoPorId(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Producto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerProductosPorCategoria(categoria: String): Result<List<Producto>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerProductosPorCategoria(categoria)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun crearProducto(request: ProductoRequest): Result<Producto> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.crearProducto(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al crear: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun actualizarProducto(id: Long, request: ProductoRequest): Result<Producto> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.actualizarProducto(id, request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al actualizar: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun eliminarProducto(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.eliminarProducto(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}