package com.example.supletanes.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.supletanes.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(onContinueClicked: () -> Unit) {
    val visibleImage = remember { mutableStateOf(false) }
    val visibleText = remember { mutableStateOf(false) }
    val visibleButton = remember { mutableStateOf(false) }
    val buttonScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        visibleImage.value = true
        delay(300)
        visibleText.value = true
        delay(200)
        visibleButton.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen con slide vertical
        AnimatedVisibility(
            visible = visibleImage.value,
            modifier = Modifier,
            enter = fadeIn( animationSpec = tween(600, easing = FastOutSlowInEasing) )
                    + slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + EnterTransition.None // Para asegurar que no haya expansión
        ) {
            Image(
                painter = painterResource(id = R.drawable.welogo),
                contentDescription = "Imagen de bienvenida",
                modifier = Modifier
                    .size(180.dp)

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto con slide horizontal
        AnimatedVisibility(
            visible = visibleText.value,
            enter = fadeIn(tween(500)) + slideInHorizontally(initialOffsetX = { -100 })
        ) {
            Text(
                text = "Bienvenido a Supletanes",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Botón con animación de pulsación
        AnimatedVisibility(
            visible = visibleButton.value,
            enter = scaleIn(initialScale = 0.6f, animationSpec = tween(500)) + fadeIn(tween(500))
        ) {
            Button(
                onClick = {
                    scope.launch {
                        buttonScale.animateTo(0.95f, tween(100))
                        buttonScale.animateTo(1f, tween(100))
                        onContinueClicked()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .graphicsLayer {
                        scaleX = buttonScale.value
                        scaleY = buttonScale.value
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Continuar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

// Ver el diseño sin ejecutar la app
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onContinueClicked = {})
}
