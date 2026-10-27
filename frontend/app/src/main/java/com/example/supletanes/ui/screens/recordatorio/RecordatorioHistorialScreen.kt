package com.example.supletanes.ui.screens.recordatorio

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.components.TimePickerDialog
import com.example.supletanes.ui.screens.recordatorio.viewmodel.RecordatorioHistorialViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioHistorialScreen() {
    val context = LocalContext.current
    val viewModel: RecordatorioHistorialViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecordatorioHistorialViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog states from ViewModel
    val showCalendarDialog by viewModel.showCalendarDialog.collectAsState()
    val showTimeDialog by viewModel.showTimeDialog.collectAsState()
    val showMensajeDialog by viewModel.showMensajeDialog.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()

    // States for the pickers and dialogs
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var mensajeRecordatorio by remember { mutableStateOf("") }
    
    // Estado local para mensaje en edición
    var mensajeEdicion by remember { mutableStateOf("") }

    // Efecto para sincronizar mensaje al abrir edición
    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            mensajeEdicion = viewModel.recordatorioEnEdicion?.mensaje ?: ""
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = if (uiState.showUndoAction) "DESHACER" else null,
                duration = SnackbarDuration.Short
            )
            
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.deshacerEliminacion()
            }
            viewModel.limpiarMensaje()
        }
    }

    // --- Dialogs (reusing PlanScreen's logic + Edit logic) ---
    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onDismissCalendar() },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            viewModel.onDateSelected(selectedMillis)
                        }
                    }
                ) { Text("Siguiente") }
            },
            dismissButton = { TextButton(onClick = { viewModel.onDismissCalendar() }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimeDialog) {
        TimePickerDialog(
            onDismissRequest = { viewModel.onDismissTimeDialog() },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onTimeSelected(timePickerState.hour, timePickerState.minute) }
                ) { Text("Siguiente") }
            },
            dismissButton = { TextButton(onClick = { viewModel.onDismissTimeDialog() }) { Text("Cancelar") } }
        ) { TimePicker(state = timePickerState) }
    }

    // Dialogo de Creación
    if (showMensajeDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissMensajeDialog() },
            title = { Text("Mensaje del Recordatorio") },
            text = {
                Column {
                    Text("Escribe el mensaje para tu recordatorio.")
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = mensajeRecordatorio,
                        onValueChange = { mensajeRecordatorio = it },
                        label = { Text("Mensaje") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.crearRecordatorio(mensajeRecordatorio)
                    mensajeRecordatorio = "" // Limpiar campo
                }) { Text("Crear") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissMensajeDialog() }) { Text("Cancelar") }
            }
        )
    }
    
    // Dialogo de Edición
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissEditDialog() },
            title = { Text("Editar Recordatorio") },
            text = {
                Column {
                    TextField(
                        value = mensajeEdicion,
                        onValueChange = { mensajeEdicion = it },
                        label = { Text("Mensaje") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val fechaHora = viewModel.getFechaHoraEdicion()
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                    Text("Fecha y Hora: ${fechaHora?.format(formatter) ?: "No definida"}", style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { viewModel.onChangeDateClick() }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Fecha")
                        }
                        OutlinedButton(onClick = { viewModel.onChangeTimeClick() }) {
                            Icon(Icons.Default.Schedule, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Hora")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.guardarEdicion(mensajeEdicion) }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissEditDialog() }) { Text("Cancelar") }
            }
        )
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onCalendarIconClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Recordatorio")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading && uiState.recordatorios.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.recordatorios.isEmpty()) {
                Text("No tienes recordatorios.", modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                    items(uiState.recordatorios) { recordatorio ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(recordatorio.mensaje, style = MaterialTheme.typography.bodyLarge)
                                Spacer(Modifier.height(4.dp))
                                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                                Text(recordatorio.fechaHora.format(formatter), style = MaterialTheme.typography.bodySmall)
                                Row(modifier = Modifier.align(Alignment.End)) {
                                    IconButton(onClick = { viewModel.onEditClick(recordatorio) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = { viewModel.eliminarRecordatorio(recordatorio) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
