package es.unex.giiis.asee.spanishweather.api.clasesapi

import com.google.gson.annotations.SerializedName


data class Localidad (

    @SerializedName("location" ) var location : Location? = Location(),
    @SerializedName("current"  ) var current  : Current?  = Current(),
    @SerializedName("forecast" ) var forecast : Forecast? = Forecast()

)
