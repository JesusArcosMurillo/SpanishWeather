package es.unex.giiis.asee.spanishweather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giiis.asee.spanishweather.database.Usuario

@Dao
interface UserDAO {

    /* Le pasamos el usuario (String) y nos devolverá un objeto de tipo Usuarioç
       No se incluye la sentencia 'LIMIT 1' ya que el usuario será clave primaria
     */
    @Query("SELECT * FROM Usuarios WHERE usuario LIKE :usuario")
    suspend fun buscarUsuario(usuario: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(user: Usuario): Long

    @Transaction
    suspend fun insertarUsuarioConControlDuplicados(usuario: Usuario): Boolean {
        val idUsuarioInsertado = insertarUsuario(usuario)
        return idUsuarioInsertado != -1L  // -1L indica un conflicto único
    }
}
