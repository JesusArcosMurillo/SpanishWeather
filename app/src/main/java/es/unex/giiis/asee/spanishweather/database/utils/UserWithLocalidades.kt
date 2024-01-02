package es.unex.giiis.asee.spanishweather.database.utils

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import es.unex.giiis.asee.spanishweather.database.clases.Location
import es.unex.giiis.asee.spanishweather.database.clases.Usuario

data class UserWithLocalidades(
    @Embedded val user: Usuario,
    @Relation(
        parentColumn = "userName",
        entityColumn = "name",
        associateBy = Junction(UserLocalidadCrossRef::class)
    )
    val pueblos: List<Location> )
