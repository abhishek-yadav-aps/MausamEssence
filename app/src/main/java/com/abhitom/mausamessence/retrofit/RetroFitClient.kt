package com.abhitom.mausamessence.retrofit

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitClient {
    val service: Service
    var idToken=""

    companion object {
        private var retroFitClient: RetroFitClient? = null

        val instance: RetroFitClient
            get() {
                if (retroFitClient == null) {
                    retroFitClient = RetroFitClient()
                }
                return retroFitClient as RetroFitClient
            }
    }

    init {

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        service=retrofit.create(Service::class.java)

    }
}