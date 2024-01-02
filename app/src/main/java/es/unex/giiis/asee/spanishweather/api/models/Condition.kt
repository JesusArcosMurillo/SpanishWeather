package es.unex.giiis.asee.spanishweather.api.models

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Condition(
    val code: Int,
    val icon: String,
    val text: String
) : Serializable