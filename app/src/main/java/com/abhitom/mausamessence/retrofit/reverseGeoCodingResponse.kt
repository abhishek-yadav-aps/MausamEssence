package com.abhitom.mausamessence.retrofit

import com.google.gson.annotations.SerializedName


data class ReverseGeoCodingResponse(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)
