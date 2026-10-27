package com.example.supletanes.data.model

import java.time.LocalDateTime

data class RecordatorioRequest(
    val fechaHora: LocalDateTime,
    val idUsuario: String,
    val mensaje: String
)