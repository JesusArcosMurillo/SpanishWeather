package es.unex.giiis.asee.spanishweather.database.utils

import androidx.room.Entity
import androidx.room.ForeignKey
import es.unex.giiis.asee.spanishweather.database.clases.Location

@Entity(
    primaryKeys = ["userName", "name"],
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = ["name"],
            childColumns = ["name"],
            onDelete = ForeignKey.CASCADE ) ] )
data class UserLocalidadCrossRef(
    val userName: String,
    val name: String
)

