package com.example.supletanes.ui.screens.products

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.supletanes.data.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ProductsScreenState(
    val productos: List<Producto>,
    val visibleItems: MutableMap<Long, Boolean>,
    val editedItemId: MutableState<Long?>,
    val showDialog: Boolean,
    val productoAEditar: Producto?,
    val onShowDialogChange: (Boolean) -> Unit,
    val onEditChange: (Producto?) -> Unit,
    val onEditedItem: (Long) -> Unit,
    val mostrarSnackbarConDeshacer: (Producto) -> Unit
)

@Composable
fun rememberProductsScreenState(
    productos: List<Producto>,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onRestoreProducto: (Producto) -> Unit
): ProductsScreenState {
    val visibleItems = remember { mutableStateMapOf<Long, Boolean>() }
    val editedItemId = remember { mutableStateOf<Long?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    // Inicializar visibilidad de items
    LaunchedEffect(productos) {
        productos.forEach { producto ->
            if (!visibleItems.containsKey(producto.id)) {
                visibleItems[producto.id] = true
            }
        }
    }

    val mostrarSnackbarConDeshacer: (Producto) -> Unit = { producto ->
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "${producto.nombre} eliminado",
                actionLabel = "Deshacer"
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRestoreProducto(producto)
                visibleItems[producto.id] = true
            }
        }
    }

    return ProductsScreenState(
        productos = productos,
        visibleItems = visibleItems,
        editedItemId = editedItemId,
        showDialog = showDialog,
        productoAEditar = productoAEditar,
        onShowDialogChange = { showDialog = it },
        onEditChange = { productoAEditar = it },
        onEditedItem = { editedItemId.value = it },
        mostrarSnackbarConDeshacer = mostrarSnackbarConDeshacer
    )
}