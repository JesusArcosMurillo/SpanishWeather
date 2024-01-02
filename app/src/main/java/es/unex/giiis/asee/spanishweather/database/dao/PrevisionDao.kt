package es.unex.giiis.asee.spanishweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.utils.Provincia

@Dao
interface PrevisionDao {

   // @Query("SELECT * FROM provincia WHERE region = :region ")
    //fun getPrevisiones(region : String): LiveData<List<Provincia>>

    //@Query("SELECT count(*) FROM localidad")
    //suspend fun getNumberOfPrevisiones(): Long

   // @Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun insertAll(provincia: List<Localidad>)
}
