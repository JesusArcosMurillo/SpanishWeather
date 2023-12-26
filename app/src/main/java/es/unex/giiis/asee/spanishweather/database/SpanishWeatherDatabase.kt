package es.unex.giiis.asee.spanishweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 2, exportSchema = false)
abstract class SpanishWeatherDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    companion object {
        private var INSTANCE: SpanishWeatherDatabase? = null
        fun getInstance(context: Context): SpanishWeatherDatabase? {
            if (INSTANCE == null) {
                synchronized(SpanishWeatherDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, SpanishWeatherDatabase::class.java,
                        "findgames.db"
                    ).build() }
            }
            return INSTANCE
        } fun destroyInstance() {
            INSTANCE = null
        }
    }
}