package com.example.supletanes.ui.screens.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.supletanes.data.model.Producto

@Composable
fun AddProductDialog(
    productoInicial: Producto? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(productoInicial?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(productoInicial?.descripcion ?: "") }
    var precio by remember { mutableStateOf(productoInicial?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(productoInicial?.stock?.toString() ?: "") }
    var categoria by remember { mutableStateOf(productoInicial?.categoria ?: "") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(if (productoInicial == null) "Añadir Producto" else "Editar Producto")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val producto = Producto(
                        id = productoInicial?.id ?: 0L,
                        nombre = nombre,
                        descripcion = descripcion.ifBlank { null },
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        stock = stock.toIntOrNull() ?: 0,
                        categoria = categoria
                    )
                    onConfirm(producto)
                },
                enabled = nombre.isNotBlank() &&
                        precio.toDoubleOrNull() != null &&
                        stock.toIntOrNull() != null &&
                        categoria.isNotBlank()
            ) {
                Text(if (productoInicial == null) "Añadir" else "Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}