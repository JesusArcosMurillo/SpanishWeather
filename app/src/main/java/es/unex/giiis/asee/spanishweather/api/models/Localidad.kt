package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Localidad(
    @PrimaryKey var localidadName: String, //el nombre ya está dentro de location, pero lo añado para evitar localides repetidas
    var provincia: String?,
    @Embedded val current: Current,
    @Embedded var location: Location,
    var is_favourite: Boolean = false
) : Serializable