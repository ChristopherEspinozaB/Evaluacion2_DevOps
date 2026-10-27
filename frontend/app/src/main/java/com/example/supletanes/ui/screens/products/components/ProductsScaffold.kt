package com.example.supletanes.ui.screens.products.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supletanes.data.model.Producto
import com.example.supletanes.ui.screens.products.ProductsScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScaffold(
    state: ProductsScreenState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    isLoading: Boolean = false, // para mostrar estado cargando
    onAddProduct: (Producto) -> Unit,
    onUpdateProduct: (Producto) -> Unit,
    onDeleteProduct: (Producto) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Suplementos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoading,
                enter = scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
                exit = scaleOut(targetScale = 0.8f, animationSpec = tween(200))
            ) {
                FloatingActionButton(onClick = { state.onShowDialogChange(true) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (state.productos.isEmpty()) {
                    item {
                        Text(
                            text = "No hay productos en el catálogo. ¡Añade uno con el botón '+'!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(state.productos, key = { it.id }) { producto ->
                        val isVisible = state.visibleItems[producto.id] ?: true
                        val isEdited = state.editedItemId.value == producto.id

                        AnimatedVisibility(
                            visible = isVisible,
                            enter = if (isEdited) {
                                scaleIn(tween(300)) + fadeIn(tween(300))
                            } else {
                                fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it / 4 })
                            },
                            exit = fadeOut(tween(300)) + slideOutVertically(targetOffsetY = { it / 4 })
                        ) {
                            ProductoItem(
                                producto = producto,
                                onEdit = { state.onEditChange(producto) },
                                onDelete = {
                                    state.visibleItems[producto.id] = false
                                    scope.launch {
                                        delay(300)
                                        onDeleteProduct(producto)
                                        state.mostrarSnackbarConDeshacer(producto)
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Indicador de carga superpuesto
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Procesando...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Diálogo para añadir producto
        if (state.showDialog) {
            AddProductDialog(
                onDismissRequest = { state.onShowDialogChange(false) },
                onConfirm = { newProduct ->
                    onAddProduct(newProduct)
                    state.onShowDialogChange(false)
                }
            )
        }

        // Diálogo para editar producto
        AnimatedVisibility(
            visible = state.productoAEditar != null,
            enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.9f),
            exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.9f)
        ) {
            state.productoAEditar?.let { producto ->
                AddProductDialog(
                    productoInicial = producto,
                    onDismissRequest = { state.onEditChange(null) },
                    onConfirm = { productoEditado ->
                        onUpdateProduct(productoEditado)
                        state.onEditedItem(productoEditado.id)
                        state.onEditChange(null)

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "${productoEditado.nombre} actualizado correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    }
}