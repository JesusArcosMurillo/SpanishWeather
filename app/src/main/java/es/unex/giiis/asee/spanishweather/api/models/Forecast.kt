package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Forecast(
    val forecastday: List<Forecastday>
) : Serializable