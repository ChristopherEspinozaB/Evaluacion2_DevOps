package com.example.supletanes.ui.screens.plan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale

@Composable
fun WeekTimeline() {
    val today = Calendar.getInstance()
    val startOfWeek = today.clone() as Calendar
    // Configura el primer día de la semana como Lunes
    startOfWeek.firstDayOfWeek = Calendar.MONDAY
    // Ajusta el calendario al Lunes de la semana actual
    startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    val days = (0..6).map {
        val day = startOfWeek.clone() as Calendar
        day.add(Calendar.DAY_OF_YEAR, it)
        day
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            val isSelected = today.get(Calendar.YEAR) == day.get(Calendar.YEAR) &&
                    today.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)
            DayItem(day = day, isSelected = isSelected)
        }
    }
}

@Composable
fun DayItem(day: Calendar, isSelected: Boolean) {
    val spanishLocale = Locale("es")
    val dayName = day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, spanishLocale)
        ?.take(3)?.replaceFirstChar { it.uppercase() } ?: ""
    val dayOfMonth = day.get(Calendar.DAY_OF_MONTH)

    val backgroundModifier = if (isSelected) {
        Modifier.background(MaterialTheme.colorScheme.primary, shape = CircleShape)
    } else {
        Modifier
    }
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .then(backgroundModifier)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor
        )
        Text(
            text = dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )
    }
}