package com.example.supletanes.utils

data class SuplementoValidationResult(
    val nombreError: String? = null,
    val precioError: String? = null,
    val stockError: String? = null,
    val isValid: Boolean = false
)

fun validarSuplemento(nombre: String, precio: String, stock: String): SuplementoValidationResult {
    val precioDouble = precio.replace(',', '.').toDoubleOrNull()
    val stockInt = stock.toIntOrNull()

    val nombreError = when {
        nombre.isBlank() -> "El nombre no puede estar vacío"
        nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
        else -> null
    }

    val precioError = when {
        precioDouble == null -> "El precio debe ser un número válido"
        precioDouble <= 0.0 -> "El precio debe ser mayor a 0"
        else -> null
    }

    val stockError = when {
        stockInt == null -> "El stock debe ser un número entero"
        stockInt < 0 -> "El stock no puede ser negativo"
        else -> null
    }

    val isValid = nombreError == null && precioError == null && stockError == null

    return SuplementoValidationResult(nombreError, precioError, stockError, isValid)
}