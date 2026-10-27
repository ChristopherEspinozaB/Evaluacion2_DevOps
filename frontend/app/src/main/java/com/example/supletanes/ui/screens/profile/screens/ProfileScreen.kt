package com.example.supletanes.ui.screens.profile.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.screens.profile.viewmodel.ProfileViewModel

data class UserProfile(
    val name: String = "Juan Pérez",
    val email: String = "juan.perez@example.com"
)

@Composable
fun ProfileScreen(
    isGuest: Boolean,
    user: UserProfile?,
    onNavigateToAuth: () -> Unit,
    onNavigateToRecordatorioHistorial: () -> Unit, // Nuevo parámetro
    onChangeNameClicked: () -> Unit = {},
    onChangePasswordClicked: () -> Unit = {},
    onPrivacyClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(context.applicationContext as Application) as T
            }
        }
    )

    if (isGuest) {
        GuestProfileScreen(onLoginClicked = onLoginClicked)
    } else {
        user?.let {
            LoggedInProfileScreen(
                user = it,
                onLogoutClicked = {
                    profileViewModel.logout(onNavigateToAuth)
                },
                onNavigateToRecordatorioHistorial = onNavigateToRecordatorioHistorial, // Pasarlo
                onChangeNameClicked = onChangeNameClicked,
                onChangePasswordClicked = onChangePasswordClicked,
                onPrivacyClicked = onPrivacyClicked
            )
        }
    }
}

@Composable
private fun LoggedInProfileScreen(
    user: UserProfile,
    onLogoutClicked: () -> Unit,
    onNavigateToRecordatorioHistorial: () -> Unit, // Nuevo parámetro
    onChangeNameClicked: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onPrivacyClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            color = colorScheme.primary
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))
        Divider()

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Gestión de Cuenta",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )

        ProfileItem(
            icon = Icons.Default.Person,
            title = "Cambiar Nombre",
            onClick = onChangeNameClicked
        )
        ProfileItem(
            icon = Icons.Default.Key,
            title = "Cambiar Contraseña",
            onClick = onChangePasswordClicked
        )
        Divider()

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Opciones",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )

        // Nuevo item para el historial
        ProfileItem(
            icon = Icons.Default.History,
            title = "Historial de Recordatorios",
            onClick = onNavigateToRecordatorioHistorial
        )
        ProfileItem(
            icon = Icons.Default.Lock,
            title = "Privacidad",
            onClick = onPrivacyClicked
        )
        Divider()

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogoutClicked, // Este onClick ahora tiene la lógica correcta
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.error
            )
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", color = colorScheme.onError)
        }
    }
}

@Composable
private fun GuestProfileScreen(onLoginClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Modo Invitado",
            modifier = Modifier.size(80.dp),
            tint = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Estás en modo invitado",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Inicia sesión o crea una cuenta para gestionar tu perfil, ver tus planes y más.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Iniciar Sesión / Registrarse")
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = colorScheme.outline
        )
    }
}

@Preview(name = "Logged In Preview", showBackground = true)
@Composable
fun LoggedInProfileScreenPreview() {
    //Le pasamos un perfil de ejemplo para la vista de usuario logueado.
    ProfileScreen(
        isGuest = false,
        user = UserProfile(name = "Usuario de Prueba", email = "preview@email.com"),
        onNavigateToAuth = {},
        onNavigateToRecordatorioHistorial = {} // Añadir para el preview
    )
}

@Preview(name = "Guest Preview", showBackground = true)
@Composable
fun GuestProfileScreenPreview() {
    //Para la vista de invitado, es correcto y seguro pasarlo como nulo.
    ProfileScreen(
        isGuest = true,
        user = null,
        onNavigateToAuth = {},
        onNavigateToRecordatorioHistorial = {} // Añadir para el preview
    )
}
