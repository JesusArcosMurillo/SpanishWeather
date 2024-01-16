package es.unex.giiis.asee.spanishweather.utils

import es.unex.giiis.asee.spanishweather.database.clases.Usuario


interface UserProvider {
    fun getUser(): Usuario
}