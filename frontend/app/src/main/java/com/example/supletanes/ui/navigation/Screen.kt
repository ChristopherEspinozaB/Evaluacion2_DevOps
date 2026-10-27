package com.example.supletanes.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Auth : Screen("auth_screen")

    object Main : Screen("main_screen/{isGuest}") {
        fun createRoute(isGuest: Boolean) = "main_screen/$isGuest"
    }

    object RecordatorioHistorial : Screen("recordatorio_historial_screen")

    object ChangeName : Screen("change_name_screen")
    object ChangePassword : Screen("change_password_screen")
    object Privacy : Screen("privacy_screen")
}
