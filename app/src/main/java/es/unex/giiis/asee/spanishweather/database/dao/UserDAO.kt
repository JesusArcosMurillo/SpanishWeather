package es.unex.giiis.asee.spanishweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.utils.UserWithLocalidades

@Dao
interface UserDAO {

    /* Le pasamos el usuario (String) y nos devolverá un objeto de tipo Usuarioç
       No se incluye la sentencia 'LIMIT 1' ya que el usuario será clave primaria
     */
    @Query("SELECT * FROM Usuarios WHERE userName LIKE :usuario")
    fun buscarUsuario(usuario: String): LiveData<Usuario>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(user: Usuario): Long

}
