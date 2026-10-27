package com.example.supletanes.ui.screens.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest
import com.example.supletanes.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository = ProductoRepository() // O inyectado via DI
) : ViewModel() {
    // Lista de productos
    private val _allProducts = MutableStateFlow<List<Producto>>(emptyList())
    val allProducts: StateFlow<List<Producto>> = _allProducts.asStateFlow()

    // Estados de carga y mensajes
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensajeSnackbar = MutableStateFlow<String?>(null)
    val mensajeSnackbar: StateFlow<String?> = _mensajeSnackbar.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.update { true }

            repository.obtenerProductos()
                .onSuccess { lista ->
                    _allProducts.update { lista }
                    Log.d("ProductoViewModel", "${lista.size} productos cargados")
                }
                .onFailure { error ->
                    Log.e("ProductoViewModel", "Error al cargar productos", error)
                    _mensajeSnackbar.update {
                        if (error is java.net.SocketTimeoutException) {
                            "Timeout al cargar productos, el servidor esta despertando aún"
                        } else {
                            "Error al cargar productos"
                        }
                    }
                }

            _isLoading.update { false }
        }
    }

    fun findById(id: Long, onResult: (Producto?) -> Unit) = viewModelScope.launch {
        repository.obtenerProductoPorId(id)
            .onSuccess { onResult(it) }
            .onFailure { onResult(null) }
    }

    // Crear producto
    fun insert(producto: Producto) = viewModelScope.launch {
        _isLoading.update { true }
        _mensajeSnackbar.update { "Creando producto... esto puede tardar" }

        val request = ProductoRequest(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio,
            stock = producto.stock,
            categoria = producto.categoria
        )

        repository.crearProducto(request)
            .onSuccess {
                Log.d("ProductoViewModel", "Producto creado")
                _mensajeSnackbar.update { "Producto creado exitosamente" }
                cargarProductos()
            }
            .onFailure { error ->
                Log.e("ProductoViewModel", "Error al crear", error)
                _mensajeSnackbar.update {
                    if (error is java.net.SocketTimeoutException) {
                        "Timeout: El servidor está despertando. Intenta de nuevo en 30 segundos"
                    } else {
                        "Error al crear producto"
                    }
                }
            }

        _isLoading.update { false }
    }

    // Actualizar producto
    fun update(producto: Producto) = viewModelScope.launch {
        val request = ProductoRequest(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio,
            stock = producto.stock,
            categoria = producto.categoria
        )

        repository.actualizarProducto(producto.id, request)
            .onSuccess {
                Log.d("ProductoViewModel", "Producto actualizado")
                cargarProductos()
            }
            .onFailure { error ->
                Log.e("ProductoViewModel", "Error al actualizar", error)
                _mensajeSnackbar.update { "Error al actualizar producto" }
            }
    }

    // Eliminar producto
    fun delete(id: Long) = viewModelScope.launch {
        repository.eliminarProducto(id)
            .onSuccess {
                Log.d("ProductoViewModel", "Producto eliminado")
                cargarProductos()
            }
            .onFailure { error ->
                Log.e("ProductoViewModel", "Error al eliminar", error)
                _mensajeSnackbar.update { "Error al eliminar producto" }
            }
    }

    fun limpiarMensaje() {
        _mensajeSnackbar.update { null }
    }

    fun refrescar() {
        cargarProductos()
    }
}