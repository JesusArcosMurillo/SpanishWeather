package es.unex.giiis.asee.spanishweather.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import es.unex.giiis.asee.spanishweather.api.SpanishWeatherAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.clases.Location
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.dao.LocalidadDao
import es.unex.giiis.asee.spanishweather.database.dao.UserDAO
import es.unex.giiis.asee.spanishweather.database.utils.UserWithLocalidades
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyRegion
import es.unex.giiis.asee.spanishweather.utils.Provincia

class RepositoryLocalidades
private constructor(
    private val localidadDao: LocalidadDao,
    private val networkService: SpanishWeatherAPI
) {
    private var lastUpdateTimeMillis: Long = 0L
    private lateinit var CCAA : DummyRegion

    /*
    variable que vamos a observar, devuelve un livedata de UserWithLocalidades
    a través de ese liveData, se llevará a cabo la transformación switchmap,
    basándonos en un liveData mutable definido previamente. Es necesario para poder
    asignarle valores abajo.
    */
    private val userFilter = MutableLiveData<String>() //usado para las localidades favoritas


    /*
    switchMap es un método de extensión en LiveData. Este método realiza un mapeo reactivo,
    lo que significa que cada vez que userFilter cambia, se ejecuta la función
    proporcionada dentro de switchMap.
    */
    val locationsInFavourite: LiveData<UserWithLocalidades> = userFilter.switchMap{
            userName ->
        localidadDao.getUserWithLocalidades(userName)
    }


    fun setUserName(userName: String) {     // cada vez que ejecutemos el SetUserId, se lanza el userFilter switchMap.
        userFilter.value = userName
    }

    suspend fun setFavorite(pueblo: Location, usuario : Usuario){
        pueblo.is_favourite = true
        localidadDao.insertAndRelate(pueblo,usuario)
    }

    suspend fun setNoFavorite(pueblo: Location) {
        pueblo.is_favourite = false
        localidadDao.delete(pueblo)
    }

    suspend fun fetchShows(region : DummyRegion): List<Provincia> {
        var conjuntoDeProvincias = mutableListOf<Provincia>() //almacena un conjunto de provincias con los pronosticos

        // Habrá dos for: uno que itere sobre cada una de las provincias de una CCAA
        // y otro que itere sobre las 10 localidades más importantes de cada provincia

        for (provincia in region.listaProvincias) {
            var listaProvincia = mutableListOf<Localidad>() //almacena los pronosticos de los pueblos de una provincia
            for (pueblo in provincia.listaPueblos){
                try {
                    val pronosticoPueblo = networkService.getPronostico(
                        "5083e7829a8b426b868181535231812",pueblo, "yes", 3
                    )
                    // con la sentencia anterior cogemos el pronóstico de un solo pueblo
                    // hay que almacenar en una lista todos los pronósticos de todos los pueblos de una provincia

                    listaProvincia.add(pronosticoPueblo)

                } catch (cause: Throwable) {
                    throw Throwable("Unable to fetch data from API", cause)
                }
            }
            //una vez hemos cogido todos los pronósticos de todos los pueblos de una provincia, los almacenaremos
            //en una lista para finalmente tener una lista de provincias con sus respectivos pronósticos
            //esto se ejecutará cada vez que se recuperen los pronósticos de una provincia completa,
            //tantas veces como provincias haya en una  CCAA

            var prov = Provincia(
                nombreProvincia = provincia.nombreProvincia,
                region = region.nombreRegion,
                listaLocalidades = listaProvincia
            )

            conjuntoDeProvincias.add(prov) //añadimos al conjunto de provincias, la provincia con todos los pronosticos
        }
        // adapter.notifyDataSetChanged()
        return conjuntoDeProvincias
    }


    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
        @Volatile private var INSTANCE: RepositoryLocalidades? = null
        fun getInstance( localidadDao: LocalidadDao,
                         weatherAPI: SpanishWeatherAPI ): RepositoryLocalidades {
            return INSTANCE ?: synchronized(this) { INSTANCE ?: RepositoryLocalidades(localidadDao,
                weatherAPI).also { INSTANCE = it } }
        }
    }
}