package es.unex.giiis.asee.spanishweather.database.clases

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val lon: Double,
    @PrimaryKey val name: String,
    var is_favourite: Boolean = false,
    val region: String,
    val tz_id: String
) : Serializable