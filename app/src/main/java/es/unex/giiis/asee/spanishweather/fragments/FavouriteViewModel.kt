package es.unex.giiis.asee.spanishweather.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.spanishweather.SpanishWeatherApplication
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyRegion
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavouriteViewModel (
    private val repository : RepositoryLocalidades
): ViewModel(){
    var user: Usuario? = null
        set(value) {
            field = value
            repository.setUserName(value!!.userName)
        }
    val locationsInFavourite = repository.locationsInFavourite

    //fun setUserName (){
      //  repository.setUserName(user!!.userName)
//}

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return FavouriteViewModel(
                    (application as SpanishWeatherApplication).appContainer.repositoryLocalidad,
                ) as T
            }
        }
    }
}