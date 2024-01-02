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

class RepositoryUsers
private constructor(
    private val userDAO: UserDAO
) {

    private val userFilter = MutableLiveData<String>() //usado para el login

    val userSaved: LiveData<Usuario> = userFilter.switchMap{
            userName ->
        userDAO.buscarUsuario(userName)
    }

    fun setUserName(userName: String) {
        userFilter.value = userName
    }

    suspend fun insertarUsuario (usuario: Usuario){
        userDAO.insertarUsuario(usuario)
    }


    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
        @Volatile private var INSTANCE: RepositoryUsers? = null
        fun getInstance(userDao: UserDAO): RepositoryUsers {
            return INSTANCE ?: synchronized(this) { INSTANCE ?: RepositoryUsers(userDao).also { INSTANCE = it } }
        }
    }
}