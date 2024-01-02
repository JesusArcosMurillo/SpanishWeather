package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.unex.giiis.asee.spanishweather.database.clases.Location
import java.io.Serializable

@Entity
data class Localidad(
    val current: Current,
    val forecast: Forecast,
    @PrimaryKey var location: Location
) : Serializable