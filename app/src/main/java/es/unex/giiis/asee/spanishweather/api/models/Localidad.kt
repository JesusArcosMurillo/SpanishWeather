package es.unex.giiis.asee.spanishweather.api.models

data class Localidad(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)