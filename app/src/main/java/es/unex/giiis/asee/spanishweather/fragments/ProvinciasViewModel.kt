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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProvinciasViewModel (
    private val repository : RepositoryLocalidades
): ViewModel(){
    var user : Usuario? = null
    private val _toast = MutableLiveData<String?>()
    private val _spinner = MutableLiveData<Boolean>(false)
    val toast: LiveData<String?> get() = _toast
    val spinner: LiveData<Boolean> get() = _spinner
    val localidades = repository.locationsInRegion

     fun refresh(region: DummyRegion) {
        launchDataLoad {
            repository.tryUpdateRecentLocalidadesCache(region, user!!)
        }
    }
    suspend fun buscar(localidad: String): Localidad? {
        return withContext(Dispatchers.IO) {
            repository.buscarIndividual(localidad)
        }
    }

    fun onToastShown() {
        _toast.value = null
    }
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                _toast.value = error.message
            } finally { _spinner.value = false }
        }
    }

    fun setRegion (region : String){
        repository.setRegion(region)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ProvinciasViewModel(
                    (application as SpanishWeatherApplication).appContainer.repositoryLocalidad,
                    ) as T
            }
        }
    }
}

