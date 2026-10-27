package com.example.supletanes.ui.screens.profile.screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.screens.profile.viewmodel.PrivacyViewModel

@Composable
fun PrivacyScreen(
    //1.- Recibe para navegar hacia atras
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val privacyViewModel: PrivacyViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PrivacyViewModel(context.applicationContext as Application) as T
            }
        }
    )

    var saving by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        privacyViewModel.uiEvent.collect { event ->
            when (event) {
                is PrivacyViewModel.UiEvent.NavigateBack -> onNavigateBack()
                is PrivacyViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                    saving = false
                }
            }
        }
    }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible = true
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(400)) + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Privacidad", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))

            //Switch de boletín informativo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Boletín informativo", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Recibir promociones y noticias por correo electrónico.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = privacyViewModel.receivePromotions,
                    onCheckedChange = { privacyViewModel.onReceivePromotionsChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Switch de notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Notificaciones", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Recibir alertas y recordatorios en el dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = privacyViewModel.receiveNotifications,
                    onCheckedChange = { privacyViewModel.onReceiveNotificationsChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val hasChanges = privacyViewModel.hasChanges

            Button(
                onClick = {
                    saving = true
                    privacyViewModel.savePrivacyPreferences()
                },
                enabled = hasChanges && !saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (saving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar cambios")
                }
            }
        }
        }
    }
}
