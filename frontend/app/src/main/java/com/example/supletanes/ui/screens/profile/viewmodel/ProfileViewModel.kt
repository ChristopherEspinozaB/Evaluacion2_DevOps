package com.example.supletanes.ui.screens.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.utils.NotificationHelper
import com.example.supletanes.utils.PrivacyPreferencesManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    fun logout(onNavigateToAuth: () -> Unit) {
        viewModelScope.launch {
            // 1. Limpiar preferencias
            PrivacyPreferencesManager.clearPreferences(context)

            // 2. Mostrar notificación
            NotificationHelper.showSimpleNotification(
                context = context,
                notificationId = 4,
                title = "Has cerrado sesión",
                text = "Vuelve pronto."
            )

            // 3. Navegar
            onNavigateToAuth()
        }
    }
}