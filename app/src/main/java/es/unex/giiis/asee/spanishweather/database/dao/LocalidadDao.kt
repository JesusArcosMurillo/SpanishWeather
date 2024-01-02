package es.unex.giiis.asee.spanishweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giiis.asee.spanishweather.database.clases.Location
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.utils.UserLocalidadCrossRef
import es.unex.giiis.asee.spanishweather.database.utils.UserWithLocalidades

@Dao
interface LocalidadDao {

    @Query("SELECT * FROM Location WHERE name = :nombrePueblo")
    suspend fun findByName(nombrePueblo: String): Location


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localidad: Location) : Long

    @Delete
    suspend fun delete(localidad: Location)

    @Transaction
    @Query("SELECT * FROM usuarios where userName = :userName")
    fun getUserWithLocalidades(userName: String): LiveData<UserWithLocalidades> //NO LLEVA SUSPEND POR EL LIVEDATA

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserShow(crossRef: UserLocalidadCrossRef)

    @Transaction
    suspend fun insertAndRelate(localidad: Location, usuario: Usuario) {
        val id = insert(localidad)
        val conexion = UserLocalidadCrossRef(usuario.userName, localidad.name)
        insertUserShow(conexion)
    }
}
