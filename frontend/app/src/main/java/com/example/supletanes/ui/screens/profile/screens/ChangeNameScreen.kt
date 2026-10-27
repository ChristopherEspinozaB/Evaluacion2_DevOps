// Ruta: app/src/main/java/com/example/supletanes/ui/screens/profile/ChangeNameScreen.kt
package com.example.supletanes.ui.screens.profile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.supletanes.ui.screens.auth.viewmodel.AuthViewModel
import com.example.supletanes.utils.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNameScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentUsername = authViewModel.userState.value?.name ?: ""
    var newUsername by remember { mutableStateOf(currentUsername) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        if (newUsername.isBlank()) {
            errorMessage = "El nombre no puede estar vacío."
            return false
        }
        errorMessage = null
        return true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Nombre") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Nuevo nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errorMessage != null
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validate()) {
                        authViewModel.updateUsername(newUsername)

                        // LLAMAR A LA NOTIFICACIÓN DE CAMBIO DE NOMBRE
                        NotificationHelper.showSimpleNotification(
                            context = context,
                            notificationId = 2,
                            title = "Perfil Actualizado",
                            text = "Tu nombre ha sido cambiado a '$newUsername'."
                        )

                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}
