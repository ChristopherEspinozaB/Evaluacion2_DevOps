@file:OptIn(ExperimentalAnimationApi::class)

package com.example.supletanes.ui.screens.products

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.screens.products.components.ProductsScaffold
import com.example.supletanes.ui.screens.products.viewmodel.ProductoViewModel

@Composable
fun ProductsScreen(viewModel: ProductoViewModel = viewModel()) {

    val productos by viewModel.allProducts.collectAsState()
    val mensajeSnackbar by viewModel.mensajeSnackbar.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mostrar mensajes de error en Snackbar
    LaunchedEffect(mensajeSnackbar) {
        mensajeSnackbar?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarMensaje()
        }
    }

    val state = rememberProductsScreenState(
        productos = productos,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onRestoreProducto = { producto ->
            viewModel.insert(producto)
        }
    )

    ProductsScaffold(
        state = state,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onAddProduct = { producto ->
            viewModel.insert(producto)
        },
        onUpdateProduct = { producto ->
            viewModel.update(producto)
        },
        onDeleteProduct = { producto ->
            viewModel.delete(producto.id)
        }
    )
}