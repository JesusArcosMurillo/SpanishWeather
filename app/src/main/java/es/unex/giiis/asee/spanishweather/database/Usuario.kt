package es.unex.giiis.asee.spanishweather.database

import androidx.room.PrimaryKey
import java.io.Serializable
import androidx.room.Index
import androidx.room.Entity

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["nombre"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) var id:Long?,
    val nombre: String = "",
    val apellido: String = "",
    val contraseña: String = "",
    val usuario: String = "",
    val email: String = ""
) : Serializable {
    constructor(nombre: String, contraseña: String) : this(null, nombre = nombre, contraseña = contraseña)
}