package es.unex.giiis.asee.spanishweather.database

import es.unex.giiis.asee.spanishweather.api.models.Localidad

data class Provincia (
    val nombreProvincia : String,
    val listaLocalidades : MutableList<Localidad>
)
