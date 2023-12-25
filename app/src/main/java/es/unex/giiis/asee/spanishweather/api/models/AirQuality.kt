package es.unex.giiis.asee.spanishweather.api.models

import com.google.gson.annotations.SerializedName

data class AirQuality(
    val co: Double,
    @SerializedName("us-epa-index"   ) var usepaindex   : Int,
    @SerializedName("gb-defra-index" ) var gbdefraindex : Int,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double,

)