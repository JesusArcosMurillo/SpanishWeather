package es.unex.giiis.asee.spanishweather.database.clases

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey var userName: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val contraseña: String = "",
    val email: String = ""
) : Serializable
