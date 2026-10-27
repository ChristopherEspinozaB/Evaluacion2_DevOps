package com.example.supletanes.ui.screens.profile.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.utils.PrivacyPreferencesManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PrivacyViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    var receivePromotions by mutableStateOf(true)
        private set
    var receiveNotifications by mutableStateOf(true)
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var initialPromotions = true
    private var initialNotifications = true

    val hasChanges: Boolean
        get() = receivePromotions != initialPromotions || receiveNotifications != initialNotifications


    init {
        viewModelScope.launch {
            PrivacyPreferencesManager.getPreferences(context).collect { (promotions, notifications) ->
                receivePromotions = promotions
                receiveNotifications = notifications
                initialPromotions = promotions
                initialNotifications = notifications
            }
        }
    }

    fun onReceivePromotionsChange(newValue: Boolean) {
        receivePromotions = newValue
    }

    fun onReceiveNotificationsChange(value: Boolean) {
        receiveNotifications = value
    }

    fun savePrivacyPreferences() {
        viewModelScope.launch {
            PrivacyPreferencesManager.savePreferences(context, receivePromotions, receiveNotifications)
            _uiEvent.emit(UiEvent.ShowSnackbar("Preferencias guardadas correctamente"))
        }
    }

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}