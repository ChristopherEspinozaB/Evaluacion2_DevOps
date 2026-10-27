// Ruta del archivo: app/src/main/java/com/example/supletanes/ui/screens/plan/PlanScreen.kt

package com.example.supletanes.ui.screens.plan

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.components.TimePickerDialog
import com.example.supletanes.ui.screens.plan.components.BarcodeSearchDialog
import com.example.supletanes.ui.screens.plan.components.CalorieTracker
import com.example.supletanes.ui.screens.plan.components.FoodSearchDialog
import com.example.supletanes.ui.screens.plan.components.PlanSection
import com.example.supletanes.ui.screens.plan.components.ProgressCheckInSection
import com.example.supletanes.ui.screens.plan.components.WeekTimeline
import com.example.supletanes.ui.screens.plan.viewmodel.PlanViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(planViewModel: PlanViewModel = viewModel()) {
    /* Estado para controlar la visibilidad del mapa */
    var mapVisible by remember { mutableStateOf(false) }

    /* Envolvemos en un Box para superponer el mapa */
    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val showCalendarDialog by planViewModel.showCalendarDialog.collectAsState()
        val showTimeDialog by planViewModel.showTimeDialog.collectAsState()
        val showMensajeDialog by planViewModel.showMensajeDialog.collectAsState()
        val datePickerState = rememberDatePickerState()
        val timePickerState = rememberTimePickerState()
        var mensajeRecordatorio by remember { mutableStateOf("") }

        val desayunoImage = remember { mutableStateOf<Bitmap?>(null) }
        val almuerzoImage = remember { mutableStateOf<Bitmap?>(null) }
        val cenaImage = remember { mutableStateOf<Bitmap?>(null) }
        val snacksImage = remember { mutableStateOf<Bitmap?>(null) }

        val desayunoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) desayunoImage.value = bitmap
        }

        val almuerzoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) almuerzoImage.value = bitmap
        }

        val cenaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) cenaImage.value = bitmap
        }

        val snacksLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) snacksImage.value = bitmap
        }

        val activeSection = remember { mutableStateOf<String?>(null) }

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    when (activeSection.value) {
                        "desayuno" -> desayunoLauncher.launch()
                        "almuerzo" -> almuerzoLauncher.launch()
                        "cena" -> cenaLauncher.launch()
                        "snacks" -> snacksLauncher.launch()
                    }
                } else {
                    Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // Diálogos
        if (showCalendarDialog) {
            DatePickerDialog(
                onDismissRequest = { planViewModel.onDismissCalendar() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { selectedMillis ->
                                planViewModel.onDateSelected(selectedMillis)
                            }
                        }
                    ) { Text("Siguiente") }
                },
                dismissButton = {
                    TextButton(onClick = { planViewModel.onDismissCalendar() }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimeDialog) {
            TimePickerDialog(
                onDismissRequest = { planViewModel.onDismissTimeDialog() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            planViewModel.onTimeSelected(timePickerState.hour, timePickerState.minute)
                        }
                    ) { Text("Siguiente") }
                },
                dismissButton = {
                    TextButton(onClick = { planViewModel.onDismissTimeDialog() }) { Text("Cancelar") }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }

        if (showMensajeDialog) {
            AlertDialog(
                onDismissRequest = { planViewModel.onDismissMensajeDialog() },
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
                    TextButton(
                        onClick = {
                            planViewModel.crearRecordatorio(mensajeRecordatorio)
                        }
                    ) { Text("Crear Recordatorio") }
                },
                dismissButton = {
                    TextButton(onClick = { planViewModel.onDismissMensajeDialog() }) { Text("Cancelar") }
                }
            )
        }

        val dailyGoalCalories = 2500
        // Los collectAsState van aquí, dentro del Composable
        val foodInfo by planViewModel.foodInfo.collectAsState()
        val results by planViewModel.searchResults.collectAsState()
        var expanded by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                Text(
                    text = "Mi Plan Diario",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Button(
                    onClick = { planViewModel.onCalendarIconClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Crear Recordatorio")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear Recordatorio")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                WeekTimeline()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                CalorieTracker(
                    consumedCalories = 1200,
                    goalCalories = dailyGoalCalories,
                    protein = 80,
                    goalProtein = 150,
                    carbs = 150,
                    goalCarbs = 300,
                    fats = 50,
                    goalFats = 100
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- Sección desayuno ---
            item {
                val desayunoItems by planViewModel.desayuno.collectAsState()

                PlanSection(
                    title = "Desayuno",
                    sectionCalories = 450,
                    goalCalories = dailyGoalCalories,
                    image = desayunoImage.value,
                    onAddItemClicked = {
                        activeSection.value = "desayuno"
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                )

                // Mostrar los alimentos agregados debajo del botón
                Column(modifier = Modifier.padding(8.dp)) {
                    desayunoItems.forEach { food ->
                        Text("${food.nombre} - ${food.calorias} kcal")
                    }
                }
            }


            // --- Buscar por código/QR ---
            item {
                val showBarcodeDialog = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        planViewModel.setActiveSection("desayuno") // marcar sección activa
                        showBarcodeDialog.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar alimento por código/QR")
                }

                BarcodeSearchDialog(planViewModel, showBarcodeDialog)
            }

            // --- Buscar por nombre ---
            item {
                val showDialog = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        planViewModel.setActiveSection("desayuno") // marcar sección activa
                        showDialog.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar alimento por nombre")
                }

                FoodSearchDialog(planViewModel, showDialog)
            }

            // --- Separador final ---
            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                PlanSection(
                    title = "Almuerzo",
                    sectionCalories = 600,
                    goalCalories = dailyGoalCalories,
                    image = almuerzoImage.value,
                    onAddItemClicked = {
                        activeSection.value = "almuerzo"
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                PlanSection(
                    title = "Cena",
                    sectionCalories = 150,
                    goalCalories = dailyGoalCalories,
                    image = cenaImage.value,
                    onAddItemClicked = {
                        activeSection.value = "cena"
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                PlanSection(
                    title = "Snacks",
                    sectionCalories = 0,
                    goalCalories = dailyGoalCalories,
                    image = snacksImage.value,
                    onAddItemClicked = {
                        activeSection.value = "snacks"
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 16.dp))

                ProgressCheckInSection(
                    userGoal = "Perder 5 kg",
                    initialWeight = 85.0,
                    currentWeight = 82.5,
                    onWeightCheckInClicked = {
                        println("Iniciando Check-in de Peso...")
                    }
                )
            }

            /* Botón que activa el mapa */
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { mapVisible = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conoce tus alrededores")
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espacio final
            }
        }

        /* El mapa, que solo se muestra si el estado es 'true' */
        AnimatedVisibility(
            visible = mapVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { factoryContext ->
                    Configuration.getInstance().load(factoryContext, factoryContext.getSharedPreferences("osmdroid", 0))
                    MapView(factoryContext).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(13.5)
                        controller.setCenter(GeoPoint(-33.4489, -70.6693))
                    }
                }
            )

            // Botón para cerrar el mapa
            IconButton(
                onClick = { mapVisible = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar mapa",
                    tint = Color.Black
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PlanScreenPreview() {
    // Para que el Preview no falle, pasamos un ViewModel de prueba
    PlanScreen()
}