package es.unex.giiis.asee.spanishweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.utils.UserLocalidadCrossRef
import es.unex.giiis.asee.spanishweather.database.utils.UserWithLocalidades

@Dao
interface LocalidadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localidad: Localidad) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE) //Replace porque la información tiene que mantererse actualizada
    suspend fun insertAll(shows: List<Localidad>)

    @Transaction
    suspend fun insertShowSavingFavourite(localidad: Localidad, usuario: Usuario) {
        val id = insert(localidad)
        val conexion = UserLocalidadCrossRef(usuario.userName, localidad.localidadName!!)
        insertUserLocalidad(conexion)
    }

    @Delete
    suspend fun delete(userLocalidad: UserLocalidadCrossRef)

    @Query("SELECT * FROM Localidad WHERE localidadName= :localidad")
    suspend fun getLocalidad(localidad : String): Localidad?

    @Query("SELECT * FROM Localidad WHERE region= :regionABuscar")
    fun getLocalidades(regionABuscar : String): LiveData<List<Localidad>>

    @Query("SELECT count(*) FROM Localidad WHERE region= :regionABuscar") //número de localidades de una region
    suspend fun getNumberOfLocalidades(regionABuscar: String): Long

    @Query("SELECT * FROM Localidad WHERE provincia= :provinciaABuscar") //número de localidades de una region
    suspend fun getLocalidadesByProvince(provinciaABuscar: String): List<Localidad>

    @Transaction
    @Query("SELECT * FROM usuarios where userName = :userName")
    fun getUserWithLocalidades(userName: String): LiveData<UserWithLocalidades> //NO LLEVA SUSPEND POR EL LIVEDATA

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserLocalidad(crossRef: UserLocalidadCrossRef)

    @Transaction
    suspend fun insertAndRelate(localidad: Localidad, usuario: Usuario) {
        val id = insert(localidad)
        val conexion = UserLocalidadCrossRef(usuario.userName, localidad.localidadName!!)
        insertUserLocalidad(conexion)
    }

    @Update
    suspend fun update (localidad : Localidad)
}
