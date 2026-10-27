package com.example.supletanes.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.supletanes.R

object NotificationHelper {

    private const val CHANNEL_ID = "supletanes_channel_id"
    private const val CHANNEL_NAME = "Notificaciones de SupletaNes"
    private const val CHANNEL_DESCRIPTION = "Notificaciones generales de la aplicación"

    // Se llama una sola vez desde MainActivity.
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Registrar el canal en el sistema
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun showSimpleNotification(
        context: Context,
        notificationId: Int,
        title: String,
        text: String
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // La notificación se cierra al tocarla

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}