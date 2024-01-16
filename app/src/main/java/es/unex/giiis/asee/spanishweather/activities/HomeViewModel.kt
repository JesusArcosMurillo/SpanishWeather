package es.unex.giiis.asee.spanishweather.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.clases.Usuario

class HomeViewModel: ViewModel() {

    private val _user = MutableLiveData<Usuario>(null)
    val user: LiveData<Usuario>
        get() = _user
    var userInSession: Usuario? = null
        set(value) {
            field = value
            _user.value = value!!
        }

    private val _localidad = MutableLiveData<String>(null)
    val localidad: LiveData<String>
        get() = _localidad

    var localidadBuscada: String? = null
        set(value) {
            field = value
            _localidad.value = value!!
        }

    private val _navigateToShow = MutableLiveData<Localidad>(null)
    val navigateToShow: LiveData<Localidad>
        get() = _navigateToShow
    fun onLocalidadClick(localidad: Localidad) {
        _navigateToShow.value = localidad
    }
}