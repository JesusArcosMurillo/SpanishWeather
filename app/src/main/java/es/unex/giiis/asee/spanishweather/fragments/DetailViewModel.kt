package es.unex.giiis.asee.spanishweather.fragments

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.spanishweather.SpanishWeatherApplication
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import kotlinx.coroutines.launch

class DetailViewModel (
    private val repository : RepositoryLocalidades
): ViewModel(){
    var user : Usuario? = null
    var localidad : Localidad? = null
    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?> get() = _toast
    fun onToastShown() {
        _toast.value = null
    }

    fun onFavoriteButtonClick() {
        viewModelScope.launch {
            localidad!!.is_favourite = true
            repository.setFavorite(localidad!!, user!!) //LLAMADA AL REPOSITORY
            _toast.value = "Añadido a favoritos"
        }
    }
    fun onNoFavoriteButtonClick(){
        viewModelScope.launch {
            localidad!!.is_favourite = false
            repository.setNoFavorite(localidad!!, user!!) //LLAMADA AL REPOSITORY
            _toast.value = "Eliminado de favoritos"
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return DetailViewModel(
                    (application as SpanishWeatherApplication).appContainer.repositoryLocalidad,
                ) as T
            }
        }
    }
}