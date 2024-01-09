package es.unex.giiis.asee.spanishweather

import android.app.Application
import es.unex.giiis.asee.spanishweather.utils.AppContainer

/**
Cada una de nuestras aplicaciones tendrán un objeto a nivel de aplicación que gestiona
toda la aplicación.
 */
class SpanishWeatherApplication : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}