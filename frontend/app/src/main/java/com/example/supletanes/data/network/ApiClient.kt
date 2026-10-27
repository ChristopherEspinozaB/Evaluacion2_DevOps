package com.example.supletanes.data.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.supletanes.data.api.ProductoApiService
import com.example.supletanes.data.network.adapter.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object ApiClient {

    // URL base para conectar con el backend desplegado en Render
    private const val BASE_URL = "https://supletanesappfinal-production.up.railway.app/"

    // Interceptor para loggear las peticiones y respuestas (muy útil para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Se añaden timeouts para dar tiempo al servidor de Render a "despertar"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Aumentar timeout de conexión
        .readTimeout(30, TimeUnit.SECONDS)    // Aumentar timeout de lectura
        .build()

    // Crear una instancia de Gson con el adaptador personalizado para LocalDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    // Construcción de la instancia de Retrofit usando el Gson personalizado
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // Exponer el servicio de Recordatorio
    val recordatorioApiService: RecordatorioApiService by lazy {
        retrofit.create(RecordatorioApiService::class.java)
    }

    // Exponer el servicio de Producto
    val productoApiService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }
}
