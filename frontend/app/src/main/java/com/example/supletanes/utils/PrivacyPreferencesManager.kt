package com.example.supletanes.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "privacy_prefs")

object PrivacyPreferencesManager {
    private val RECEIVE_PROMOTIONS = booleanPreferencesKey("receive_promotions")
    private val RECEIVE_NOTIFICATIONS = booleanPreferencesKey("receive_notifications")

    suspend fun savePreferences(context: Context, promotions: Boolean, notifications: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECEIVE_PROMOTIONS] = promotions
            prefs[RECEIVE_NOTIFICATIONS] = notifications
        }
    }

    fun getPreferences(context: Context): Flow<Pair<Boolean, Boolean>> {
        return context.dataStore.data.map { prefs ->
            val promotions = prefs[RECEIVE_PROMOTIONS] ?: true
            val notifications = prefs[RECEIVE_NOTIFICATIONS] ?: true
            Pair(promotions, notifications)
        }
    }

    suspend fun clearPreferences(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}