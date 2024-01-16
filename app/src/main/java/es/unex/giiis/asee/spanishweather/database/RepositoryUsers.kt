package es.unex.giiis.asee.spanishweather.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.database.dao.UserDAO

class RepositoryUsers(
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
}