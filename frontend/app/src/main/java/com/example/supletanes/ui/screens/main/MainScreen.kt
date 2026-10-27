package com.example.supletanes.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.supletanes.ui.components.AnimatedScreen
import com.example.supletanes.ui.navigation.BottomNavItem
import com.example.supletanes.ui.screens.plan.PlanScreen
import com.example.supletanes.ui.screens.products.ProductsScreen
import com.example.supletanes.ui.screens.profile.screens.ProfileScreen
import com.example.supletanes.ui.screens.profile.screens.UserProfile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    isGuest: Boolean,
    userProfile: UserProfile?,
    onLogoutClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToRecordatorioHistorial: () -> Unit, // Nuevo parámetro
    onChangeNameClicked: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onPrivacyClicked: () -> Unit
) {
    val mainNavController = rememberNavController()

    val visible = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        visible.value = true
        }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val items = listOf(
                    BottomNavItem.Plan,
                    BottomNavItem.Products,
                    BottomNavItem.Profile
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        label = { Text(item.title) },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        onClick = {
                            mainNavController.navigate(item.route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = BottomNavItem.Plan.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Plan.route) {
                AnimatedScreen {
                    PlanScreen()
                }
            }
            composable(BottomNavItem.Products.route) {
                AnimatedScreen {
                    ProductsScreen()
                }
            }
            composable(BottomNavItem.Profile.route) {
                AnimatedScreen {
                    ProfileScreen(
                        isGuest = isGuest,
                        user = userProfile,
                        onNavigateToAuth = onLogoutClicked,
                        onLoginClicked = onLoginClicked,
                        onNavigateToRecordatorioHistorial = onNavigateToRecordatorioHistorial, // Pasarlo aquí
                        onChangeNameClicked = onChangeNameClicked,
                        onChangePasswordClicked = onChangePasswordClicked,
                        onPrivacyClicked = onPrivacyClicked
                    )
                }
            }
        }
    }
}
