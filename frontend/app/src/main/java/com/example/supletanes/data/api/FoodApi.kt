package com.example.supletanes.data.api

import com.example.supletanes.data.model.FoodDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("api/food/{barcode}")
    suspend fun getFood(@Path("barcode") barcode: String): FoodDTO

    @GET("api/food/search")
    suspend fun searchFood(@Query("name") name: String): List<FoodDTO>
}