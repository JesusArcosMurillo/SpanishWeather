package es.unex.giiis.asee.spanishweather.utils

// Representa el contenido de cada una de las opciones del desplegable de las CCAA
data class CCAAOption(
    val name: String, //nombre de la CCAA
    val imageResId: Int //referencia de la imagen de la bandera en los layout
){
    override fun toString(): String {
    return name
    }
}

