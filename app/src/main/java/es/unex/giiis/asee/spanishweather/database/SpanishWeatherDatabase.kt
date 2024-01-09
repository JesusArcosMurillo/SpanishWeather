package es.unex.giiis.asee.spanishweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.dao.LocalidadDao
import es.unex.giiis.asee.spanishweather.database.dao.UserDAO
import es.unex.giiis.asee.spanishweather.database.utils.UserLocalidadCrossRef

@Database(entities = [Usuario::class, Localidad::class,
                     UserLocalidadCrossRef::class],
    version = 1, exportSchema = false)
abstract class SpanishWeatherDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun localidadDao(): LocalidadDao
    companion object {
        private var INSTANCE: SpanishWeatherDatabase? = null
        fun getInstance(context: Context): SpanishWeatherDatabase? {
            if (INSTANCE == null) {
                synchronized(SpanishWeatherDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, SpanishWeatherDatabase::class.java,
                        "spanishweather.db"
                    ).build() }
            }
            return INSTANCE
        } fun destroyInstance() {
            INSTANCE = null
        }
    }
}