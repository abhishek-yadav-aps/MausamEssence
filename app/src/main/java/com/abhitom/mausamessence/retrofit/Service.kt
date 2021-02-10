package com.abhitom.mausamessence.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("onecall")
    fun oneCallApi(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String, @Query("units") units: String): Call<OneCallResponse>
}