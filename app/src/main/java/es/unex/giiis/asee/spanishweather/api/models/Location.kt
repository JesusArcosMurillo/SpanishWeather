package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
) : Serializable