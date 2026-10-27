package com.example.supletanes.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(tween(300)) + slideInHorizontally(initialOffsetX = { it / 2 }) + scaleIn(initialScale = 0.95f),
        exit = fadeOut(tween(200)) + slideOutHorizontally(targetOffsetX = { -it / 2 }),
        modifier = modifier
    ) {
        content()
    }
}