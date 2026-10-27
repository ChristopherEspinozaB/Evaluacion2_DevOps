package com.example.supletanes.ui.screens.plan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CalorieTracker(
    consumedCalories: Int,
    goalCalories: Int,
    protein: Int,
    goalProtein: Int,
    carbs: Int,
    goalCarbs: Int,
    fats: Int,
    goalFats: Int
) {
    val remainingCalories = goalCalories - consumedCalories
    val progress by animateFloatAsState(
        targetValue = if (goalCalories > 0) consumedCalories.toFloat() / goalCalories.toFloat() else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Seguimiento de Calorías", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalorieInfo("Consumidas", consumedCalories)
                CalorieInfo("Restantes", remainingCalories)
                CalorieInfo("Meta", goalCalories)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MacroNutrientInfo("Proteínas", protein, goalProtein)
                MacroNutrientInfo("Carbs", carbs, goalCarbs)
                MacroNutrientInfo("Grasas", fats, goalFats)
            }
        }
    }
}

@Composable
fun CalorieInfo(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = "$value kcal", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun MacroNutrientInfo(label: String, consumed: Int, goal: Int) {
    val progress by animateFloatAsState(
        targetValue = if (goal > 0) consumed.toFloat() / goal.toFloat() else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = "$consumed / $goal g", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.width(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalorieTrackerPreview() {
    CalorieTracker(
        consumedCalories = 1200,
        goalCalories = 2500,
        protein = 80,
        goalProtein = 150,
        carbs = 150,
        goalCarbs = 300,
        fats = 50,
        goalFats = 70
    )
}