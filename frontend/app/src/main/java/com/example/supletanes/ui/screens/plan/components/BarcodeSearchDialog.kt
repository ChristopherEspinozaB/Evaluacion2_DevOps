package com.example.supletanes.ui.screens.plan.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.supletanes.ui.screens.plan.viewmodel.PlanViewModel

@Composable
fun BarcodeSearchDialog(
    planViewModel: PlanViewModel,
    showDialog: MutableState<Boolean>
) {
    var barcode by remember { mutableStateOf("") }
    val foodInfo by planViewModel.foodInfo.collectAsState()

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Buscar alimento por código", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        label = { Text("Código de barras / QR") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val isLoading by planViewModel.isLoading.collectAsState()

                    Button(
                        onClick = { planViewModel.buscarAlimento(barcode) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Buscar")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        foodInfo?.let { info ->
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Nombre: ${info.nombre}")
                                Text("Calorías: ${info.calorias}")
                                Text("Proteínas: ${info.proteinas} g")
                                Text("Carbohidratos: ${info.carbohidratos} g")
                                Text("Grasas: ${info.grasas} g")
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