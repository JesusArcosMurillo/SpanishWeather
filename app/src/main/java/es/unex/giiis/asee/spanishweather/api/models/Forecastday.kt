package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
) : Serializable