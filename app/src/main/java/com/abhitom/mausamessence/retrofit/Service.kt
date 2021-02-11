package com.abhitom.mausamessence.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("data/2.5/onecall")
    fun oneCallApi(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String, @Query("units") units: String): Call<OneCallResponse>

    @GET("geo/1.0/reverse")
    fun reverseGeoCoding(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String, @Query("limit") limit: Int): Call<List<ReverseGeoCodingResponse>>
}