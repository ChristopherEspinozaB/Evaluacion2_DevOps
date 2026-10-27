package com.example.supletanes.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.supletanes.ui.screens.auth.AuthScreen
import com.example.supletanes.ui.screens.auth.viewmodel.AuthViewModel
import com.example.supletanes.ui.screens.main.MainScreen
import com.example.supletanes.ui.screens.profile.screens.ChangeNameScreen
import com.example.supletanes.ui.screens.profile.screens.ChangePasswordScreen
import com.example.supletanes.ui.screens.profile.screens.PrivacyScreen
import com.example.supletanes.ui.screens.recordatorio.RecordatorioHistorialScreen
import com.example.supletanes.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val animationSpec = tween<IntOffset>(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
    val authViewModel: AuthViewModel = viewModel()
    val userProfile = authViewModel.userState.value

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        // --- Pantalla de Bienvenida ---
        composable(
            route = Screen.Welcome.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            WelcomeScreen(
                onContinueClicked = { navController.navigate(Screen.Auth.route) }
            )
        }

        // --- Pantalla de Autenticación (Login/Registro) ---
        composable(
            route = Screen.Auth.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) {
            AuthScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.createRoute(isGuest = false)) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onContinueAsGuest = {
                    navController.navigate(Screen.Main.createRoute(isGuest = true)) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantalla Principal (Contenido de la App) ---
        composable(
            route = Screen.Main.route,
            arguments = listOf(navArgument("isGuest") { type = NavType.BoolType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) { backStackEntry ->
            val isGuest = backStackEntry.arguments?.getBoolean("isGuest") ?: true
            MainScreen(
                isGuest = isGuest,
                userProfile = userProfile,
                onLogoutClicked = {
                    authViewModel.logout()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onLoginClicked = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onNavigateToRecordatorioHistorial = { navController.navigate(Screen.RecordatorioHistorial.route) },
                onChangeNameClicked = { navController.navigate(Screen.ChangeName.route) },
                onChangePasswordClicked = { navController.navigate(Screen.ChangePassword.route) },
                onPrivacyClicked = { navController.navigate(Screen.Privacy.route) }
            )
        }

        // --- Pantalla de Historial de Recordatorios ---
        composable(
            route = Screen.RecordatorioHistorial.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) {
            RecordatorioHistorialScreen() // No more params needed
        }

        // --- Pantallas Secundarias (Formularios de Perfil) ---
        composable(
            route = Screen.ChangeName.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) {
            ChangeNameScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ChangePassword.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) {
            ChangePasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Privacy.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = animationSpec) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = animationSpec) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) }
        ) {
            PrivacyScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
