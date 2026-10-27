package com.example.supletanes.data.api

import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {

    @GET("api/productos")
    suspend fun obtenerProductos(): Response<List<Producto>>

    @GET("api/productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Long): Response<Producto>

    @GET("api/productos/categoria/{categoria}")
    suspend fun obtenerProductosPorCategoria(@Path("categoria") categoria: String): Response<List<Producto>>

    @POST("api/productos")
    suspend fun crearProducto(@Body request: ProductoRequest): Response<Producto>

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body request: ProductoRequest
    ): Response<Producto>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Unit>
}