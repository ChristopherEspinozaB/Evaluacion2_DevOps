package com.example.supletanes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Plan : BottomNavItem(
        title = "Plan",
        icon = Icons.Default.Home,
        route = "plan_route"
    )

    object Products : BottomNavItem(
        title = "Productos",
        icon = Icons.Default.ShoppingBag,
        route = "products_route"
    )
    object Profile : BottomNavItem(
        title = "Perfil",
        icon = Icons.Default.Person,
        route = "profile_route"
    )
}