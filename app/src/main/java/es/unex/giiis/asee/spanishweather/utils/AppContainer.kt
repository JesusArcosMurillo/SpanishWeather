package es.unex.giiis.asee.spanishweather.utils

import android.content.Context
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.RepositoryUsers
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase

class AppContainer(context: Context?) {
    private val networkService = conexionAPI()
    private val db = SpanishWeatherDatabase.getInstance(context!!)
    val repositoryLocalidad = RepositoryLocalidades(db!!.localidadDao(),conexionAPI())
    val repositoryUsuario = RepositoryUsers(db!!.userDao())
}