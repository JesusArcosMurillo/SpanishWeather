package es.unex.giiis.asee.spanishweather.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.spanishweather.SpanishWeatherApplication
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.RepositoryUsers
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyRegion
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel (
    private val repository : RepositoryUsers
): ViewModel(){

    fun insertarUsuario(usuario : Usuario){
        viewModelScope.launch {
            repository.insertarUsuario(usuario)
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

                return RegisterViewModel(
                    (application as SpanishWeatherApplication).appContainer.repositoryUsuario,
                ) as T
            }
        }
    }
}