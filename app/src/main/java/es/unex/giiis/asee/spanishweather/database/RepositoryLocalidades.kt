package es.unex.giiis.asee.spanishweather.database


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import es.unex.giiis.asee.spanishweather.api.APIError
import es.unex.giiis.asee.spanishweather.api.SpanishWeatherAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.api.models.Location
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.dao.LocalidadDao
import es.unex.giiis.asee.spanishweather.database.utils.UserLocalidadCrossRef
import es.unex.giiis.asee.spanishweather.database.utils.UserWithLocalidades
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyRegion
import es.unex.giiis.asee.spanishweather.utils.Provincia
import retrofit2.HttpException
import java.net.UnknownHostException

class RepositoryLocalidades(
    private val localidadDao: LocalidadDao,
    private val networkService: SpanishWeatherAPI
) {
    private var lastUpdateTimeMillis: Long = 0L
    val ultimaActualizacionMap = mutableMapOf<String, Long>()


    ///////////////////////////////////////////////////////////////////////////////
    // ESTOS LIVEDATA SE USARÁN PARA LA PARAMETRIZACIÓN DE LAS LOCALIDADES FAVORITAS
    ///////////////////////////////////////////////////////////////////////////////

    /**
    variable que vamos a observar, devuelve un livedata de UserWithLocalidades
    a través de ese liveData, se llevará a cabo la transformación switchmap,
    basándonos en un liveData mutable definido previamente. Es necesario para poder
    asignarle valores abajo.
    */
    private val userFilter = MutableLiveData<String>() //usado para las localidades favoritas


    /**
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


    ///////////////////////////////////////////////////////////////////////////////
    // ESTOS LIVEDATA SE USARÁN PARA LA PARAMETRIZACIÓN DE LAS LOCALIDADES EN FUNCIÓN DE LA REGIÓN
    ///////////////////////////////////////////////////////////////////////////////
    private val regionFilter = MutableLiveData<String>() //usado para las localidades por region

    val locationsInRegion: LiveData<List<Localidad>> = regionFilter.switchMap{
            region ->
        localidadDao.getLocalidades(region)
    }

    fun setRegion(userName: String) {     // cada vez que ejecutemos el SetRegion, se lanza el regionFilter switchMap.
       regionFilter.value = userName
    }

    suspend fun setFavorite(pueblo: Localidad, usuario : Usuario){
        localidadDao.update(pueblo)
        localidadDao.insertUserLocalidad(UserLocalidadCrossRef(usuario.userName, pueblo.localidadName))
    }

    suspend fun setNoFavorite(pueblo: Localidad, usuario : Usuario) {
        localidadDao.delete(UserLocalidadCrossRef(usuario.userName,pueblo.localidadName))
        localidadDao.update(pueblo)

    }

    suspend fun fetchLocalidades(region : DummyRegion, user : Usuario){
        var conjuntoDePueblos = mutableListOf<Localidad>() //almacena un conjunto de provincias con los pronosticos

        // Habrá dos for: uno que itere sobre cada una de las provincias de una CCAA
        // y otro que itere sobre las 10 localidades más importantes de cada provincia

        for (provincia in region.listaProvincias) {
            for (pueblo in provincia.listaPueblos){
                try {
                    val pronosticoPueblo = networkService.getPronostico(
                        "5083e7829a8b426b868181535231812",pueblo, "yes", 3
                    )
                    // con la sentencia anterior cogemos el pronóstico de un solo pueblo

                    pronosticoPueblo.localidadName=pronosticoPueblo.location.name
                    pronosticoPueblo.provincia=provincia.nombreProvincia
                    conjuntoDePueblos.add(pronosticoPueblo)
                    ultimaActualizacionMap[pronosticoPueblo.location.region] = System.currentTimeMillis()

                } catch (cause: Throwable) {
                    throw APIError("Unable to fetch data from API", cause)
                } catch (unknownHostException: UnknownHostException) {
                    throw APIError("Sin conexión a internet", unknownHostException)
                }catch (httpException: HttpException) {
                    throw APIError("Bad Request (seguramente el nombre del pueblo esté mal)", httpException)
                }
            }
            actualizarLocalidadesSalvandoFavoritos(conjuntoDePueblos, provincia.nombreProvincia, user)
            //localidadDao.insertAll(conjuntoDePueblos)
            conjuntoDePueblos.clear()
        }
    }

    suspend fun actualizarLocalidadesSalvandoFavoritos(apiLocalidades: List<Localidad>,
                                                       provincia : String, user : Usuario) {
        // Obtengo la lista de los pueblos que voy a insertar
        val localidadesActuales = localidadDao.getLocalidadesByProvince(provincia)
        val listaSinFavoritos = mutableListOf<Localidad>()
        // Compruebo si estos pueblos ya existen previamente,
        if (!localidadesActuales.isEmpty()){ //si ya están en la BD, clasificaré por favoritos y no favoritos
            for (apiLocalidad in apiLocalidades) {
                val localidadExistente = localidadesActuales.find { it.localidadName == apiLocalidad.localidadName }
                if (localidadExistente != null) {
                    if (localidadExistente.is_favourite==true){
                        apiLocalidad.is_favourite = localidadExistente.is_favourite
                        localidadDao.insertShowSavingFavourite(apiLocalidad, user)
                    }else{
                        listaSinFavoritos.add(apiLocalidad)
                    }
                }
            }
            localidadDao.insertAll(listaSinFavoritos)
        }else{ //si no están en la BD, los inserto directamente
            localidadDao.insertAll(apiLocalidades)
        }
    }


    suspend fun tryUpdateRecentLocalidadesCache(region : DummyRegion, user : Usuario) {
        if (shouldUpdateLocalidadesCache(region.nombreRegion)) fetchLocalidades(region, user)
    }


    private suspend fun shouldUpdateLocalidadesCache(region : String): Boolean {
        if (ultimaActualizacionMap.containsKey(region)) {
            lastUpdateTimeMillis = ultimaActualizacionMap[region]!!
            Log.i("Prueba", "Mensaje de información: ${ultimaActualizacionMap[region]}")

        }else{
            lastUpdateTimeMillis = 0L
        }

        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        Log.i("Prueba", "Mensaje de información: ${localidadDao.getNumberOfLocalidades(region)}")
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || localidadDao.getNumberOfLocalidades(region) == 0L
    }

    suspend fun buscarIndividual(localidad: String) : Localidad? {
        var pronosticoPueblo : Localidad? = null
        try {
            pronosticoPueblo = networkService.getPronostico(
                "5083e7829a8b426b868181535231812", localidad, "yes", 3
            )
            // con la sentencia anterior cogemos el pronóstico de un solo pueblo

            pronosticoPueblo.localidadName=pronosticoPueblo.location.name
            val localidadBuscada = localidadDao.getLocalidad(pronosticoPueblo.localidadName)
            if (localidadBuscada!=null) {
                pronosticoPueblo.is_favourite = localidadBuscada.is_favourite
                localidadDao.update(pronosticoPueblo)
            }else{
                localidadDao.insert(pronosticoPueblo)
            }
        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
        } catch (unknownHostException: UnknownHostException) {
            throw APIError("Sin conexión a internet", unknownHostException)
        }catch (httpException: HttpException) {
            throw APIError("Bad Request (seguramente el nombre del pueblo esté mal)", httpException)
        }
        return pronosticoPueblo
    }

    companion object {
        private const val MINUTES_IN_MILLIS: Long = 60000
        private const val FETCH_INTERVAL_MINUTES: Long = 15
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = FETCH_INTERVAL_MINUTES * MINUTES_IN_MILLIS
    }
}