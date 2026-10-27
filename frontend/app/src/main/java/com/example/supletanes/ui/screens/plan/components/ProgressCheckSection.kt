package com.example.supletanes.ui.screens.plan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun ProgressCheckInSection(
    userGoal: String,
    initialWeight: Double,
    currentWeight: Double,
    onWeightCheckInClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        // Puedes ajustar la elevación o colores según tu tema
        // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título de la sección
            Text(
                text = "Tu Progreso y Check-in",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Objetivo del usuario
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite, // O un ícono más específico de "Meta"
                    contentDescription = "Objetivo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Meta: $userGoal",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Resumen de Peso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Peso Inicial
                WeightDisplay(label = "Inicial", weight = initialWeight)

                // Progreso (Cambio de Peso)
                val weightChange = initialWeight - currentWeight // Asumiendo que se busca bajar
                WeightChangeDisplay(weightChange = weightChange)

                // Peso Actual
                WeightDisplay(label = "Actual", weight = currentWeight)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Check-in (Cambiar Peso)
            Button(
                onClick = onWeightCheckInClicked,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar Peso (Check-in)", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun WeightDisplay(label: String, weight: Double) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${"%.1f".format(weight)} kg",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeightChangeDisplay(weightChange: Double) {
    val changeText = if (weightChange > 0) {
        "- ${"%.1f".format(weightChange)} kg"
    } else if (weightChange < 0) {
        "+ ${"%.1f".format(abs(weightChange))} kg"
    } else {
        "0.0 kg"
    }

    val color = when {
        weightChange > 0 -> Color(0xFF4CAF50) // Verde (Pérdida de peso positiva)
        weightChange < 0 -> Color(0xFFF44336) // Rojo (Aumento de peso)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Progreso",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = changeText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressCheckInSectionPreview() {
    ProgressCheckInSection(
        userGoal = "Perder 5 kg",
        initialWeight = 85.0,
        currentWeight = 82.5,
        onWeightCheckInClicked = {}
    )
}