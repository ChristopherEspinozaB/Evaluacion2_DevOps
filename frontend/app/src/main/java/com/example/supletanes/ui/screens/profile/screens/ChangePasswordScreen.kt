package com.example.supletanes.ui.screens.profile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.screens.profile.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    // 1. Recibe la acción para navegar hacia atrás desde AppNavigation
    onNavigateBack: () -> Unit,
    changePasswordViewModel: ChangePasswordViewModel = viewModel()
) {
    // 2. Escucha los eventos del ViewModel para actuar sobre ellos
    LaunchedEffect(key1 = true) {
        changePasswordViewModel.uiEvent.collect { event ->
            when (event) {
                is ChangePasswordViewModel.UiEvent.NavigateBack -> {
                    onNavigateBack() // Ejecuta la navegación
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cambiar Contraseña",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo para la contraseña actual
        OutlinedTextField(
            value = changePasswordViewModel.currentPassword,
            onValueChange = { changePasswordViewModel.onCurrentPasswordChange(it) },
            label = { Text("Contraseña Actual") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            isError = changePasswordViewModel.currentPasswordError != null,
            supportingText = { changePasswordViewModel.currentPasswordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para la nueva contraseña
        OutlinedTextField(
            value = changePasswordViewModel.newPassword,
            onValueChange = { changePasswordViewModel.onNewPasswordChange(it) },
            label = { Text("Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            isError = changePasswordViewModel.newPasswordError != null,
            supportingText = { changePasswordViewModel.newPasswordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para confirmar la nueva contraseña
        OutlinedTextField(
            value = changePasswordViewModel.confirmPassword,
            onValueChange = { changePasswordViewModel.onConfirmPasswordChange(it) },
            label = { Text("Confirmar Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            isError = changePasswordViewModel.confirmPasswordError != null,
            supportingText = { changePasswordViewModel.confirmPasswordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { changePasswordViewModel.onSaveClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}
