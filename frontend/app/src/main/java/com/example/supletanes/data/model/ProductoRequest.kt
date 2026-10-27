package com.example.supletanes.data.model

data class ProductoRequest(
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val stock: Int,
    val categoria: String
)