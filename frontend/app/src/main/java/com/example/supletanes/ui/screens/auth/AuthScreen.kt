// Ruta: app/src/main/java/com/example/supletanes/ui/screens/auth/AuthScreen.kt
package com.example.supletanes.ui.screens.auth

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.supletanes.ui.screens.auth.viewmodel.AuthViewModel
import com.example.supletanes.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Estados para los errores de validación
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var isContentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    // Función de validación que incluye la contraseña de 6 caracteres
    fun validateForm(): Boolean {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        emailError = if (!isEmailValid) "El correo no es válido" else null
        passwordError = if (!isPasswordValid) "La contraseña debe tener al menos 6 caracteres" else null

        // Comprueba que todos los campos sean válidos
        return isEmailValid && isPasswordValid && username.isNotBlank()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isContentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 350)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = 350)
                        )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null // Limpiar error al escribir
                        },
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError != null
                    )
                    emailError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null // Limpiar error al escribir
                        },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError != null
                    )
                    passwordError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (validateForm()) {
                                isLoading = true
                                coroutineScope.launch {
                                    delay(1500) // Simula la espera de la red

                                    authViewModel.login(username = username, email = email)
                                    NotificationHelper.showSimpleNotification(
                                        context = context,
                                        notificationId = 1,
                                        title = "¡Bienvenido de nuevo, $username!",
                                        text = "Has iniciado sesión correctamente."
                                    )
                                    onLoginSuccess()
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Iniciar Sesión")
                        }
                    }

                    TextButton(
                        onClick = {
                            NotificationHelper.showSimpleNotification(
                                context = context,
                                notificationId = 3,
                                title = "Modo Invitado",
                                text = "Estás navegando como invitado."
                            )
                            onContinueAsGuest()
                        },
                        enabled = !isLoading
                    ) {
                        Text("Continuar como invitado")
                    }
                }
            }
        }
    }
}
