package es.unex.giiis.asee.spanishweather.database.utils

import androidx.room.Entity
import androidx.room.ForeignKey
import es.unex.giiis.asee.spanishweather.api.models.Localidad

@Entity(
    primaryKeys = ["userName", "localidadName"],
    foreignKeys = [
        ForeignKey(
            entity = Localidad::class,
            parentColumns = ["localidadName"],
            childColumns = ["localidadName"],
            onDelete = ForeignKey.CASCADE ) ] )
data class UserLocalidadCrossRef(
    val userName: String,
    val localidadName: String
)

