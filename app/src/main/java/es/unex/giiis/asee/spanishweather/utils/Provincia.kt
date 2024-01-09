package es.unex.giiis.asee.spanishweather.utils

import androidx.room.Entity
import es.unex.giiis.asee.spanishweather.api.models.Localidad


data class Provincia (
    val nombreProvincia : String,
    val region : String?,
    val listaLocalidades : MutableList<Localidad>
)
