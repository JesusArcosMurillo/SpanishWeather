package es.unex.giiis.asee.spanishweather.database

import es.unex.giiis.asee.spanishweather.api.clasesapi.Localidad

data class Provincias (
    val nombreProvincia : String,
    val listaLocalidades : List<Localidad>
)
