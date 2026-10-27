package com.example.supletanes.data.model

import java.time.LocalDateTime

data class Recordatorio(
    val id: Long,
    val idUsuario: String,
    val mensaje: String,
    val fechaHora: LocalDateTime
)