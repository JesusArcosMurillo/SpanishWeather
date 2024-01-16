package es.unex.giiis.asee.spanishweather.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.api.models.Location
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.ActivityHomeBinding
import es.unex.giiis.asee.spanishweather.fragments.FavouriteFragment
import es.unex.giiis.asee.spanishweather.fragments.FavouriteFragmentDirections
import es.unex.giiis.asee.spanishweather.fragments.ProvinciasFragment
import es.unex.giiis.asee.spanishweather.fragments.ProvinciasFragmentDirections
import es.unex.giiis.asee.spanishweather.utils.Provincia
import es.unex.giiis.asee.spanishweather.utils.UserProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity(){

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var isSearchButtonVisible = true
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment).navController
    }

    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: Usuario,
        ) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }

    }

     fun onLocalidadClick(localidad: Localidad) {
        //este método es para hacer click en la pantalla de discover
            val action =
                ProvinciasFragmentDirections.actionProvinciasFragmentToDetailFragment(localidad)
            navController.navigate(action)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userInSession = intent.getSerializableExtra(USER_INFO) as Usuario
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.navigateToShow.observe(this) {
                show -> show?.let {
            onLocalidadClick(show)
                }
        }
        setUpUI()
    }

    fun setUpUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.tono)
        }

        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.provinciasFragment,
                R.id.favouriteFragment,
                R.id.detailFragment
            )
        )
        setSupportActionBar(binding.toolbar) //indicarle al activity que queremos usar
        // esta toolbar como el action bar de la activity

        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        ) //configurar el action bar con nuestro navigation controller
        //a partir de la configuración de arriba

        // Ocultar la barra cuando se cambie al detalle

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.toolbar.visibility = View.GONE
                binding.bottomNavigation.visibility = View.GONE
            } else {
                isSearchButtonVisible = destination.id != R.id.favouriteFragment
                updateSearchButtonVisibility()
                binding.toolbar.visibility = View.VISIBLE
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    private fun updateSearchButtonVisibility() {
        val botonBusqueda = binding.toolbar.menu.findItem(R.id.action_search)
        botonBusqueda?.isVisible = isSearchButtonVisible
    }

    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        var listaUnica = mutableListOf<Provincia>()
        var listaUnica2 = mutableListOf<Localidad>()

        // listener para el botón de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    if(query.length!=0){
                        viewModel.localidadBuscada = query
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // es necesario sobreescrbirlo pero no vamos a realizar ninguna operación
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    private suspend fun fetchLocalidades(pueblo: String): Localidad? {
        try {
            return conexionAPI().getPronostico("5083e7829a8b426b868181535231812", pueblo, "yes", 3)
        } catch (cause: Throwable) {
            throw Throwable("Unable to fetch data from API", cause)
        }
    }
}


