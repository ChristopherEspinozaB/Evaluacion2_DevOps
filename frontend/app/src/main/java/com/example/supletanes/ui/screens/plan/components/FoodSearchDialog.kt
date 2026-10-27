package com.example.supletanes.ui.screens.plan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.supletanes.ui.screens.plan.viewmodel.PlanViewModel

@Composable
fun FoodSearchDialog(
    planViewModel: PlanViewModel,
    showDialog: MutableState<Boolean>
) {
    val results by planViewModel.searchResults.collectAsState()
    var query by remember { mutableStateOf("") }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Buscar alimento", style = MaterialTheme.typography.titleLarge)

                    val isLoading by planViewModel.isLoading.collectAsState()

                    val mensajeError by planViewModel.mensajeError.collectAsState()
                    if (mensajeError != null) {
                        Text(mensajeError!!, color = Color.Red)
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            planViewModel.buscarPorNombre(query)
                        },
                        label = { Text("Nombre del alimento") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            items(results) { food ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            planViewModel.seleccionarAlimento(food)
                                            showDialog.value = false
                                        }
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(food.nombre ?: "Sin nombre")
                                        Text("Calorías: ${food.calorias}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                Divider()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showDialog.value = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}