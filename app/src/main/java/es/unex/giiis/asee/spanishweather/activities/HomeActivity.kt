package es.unex.giiis.asee.spanishweather.activities

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.fragment.app.FragmentTransaction
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.databinding.ActivityHomeBinding
import es.unex.giiis.asee.spanishweather.fragments.FavouriteFragment
import es.unex.giiis.asee.spanishweather.fragments.ProvinciasFragment

class HomeActivity : AppCompatActivity(), ProvinciasFragment.OnShowClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var provinciasFragment: ProvinciasFragment
    private lateinit var favouriteFragment: FavouriteFragment
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment).navController }

    override fun onShowClick(localidad: Localidad) {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreate", e);
            throw e;
        }

        setUpUI()
        setUpListeners()
    }

    fun setUpUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.tono)
        }

        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.provinciasFragment,
                R.id.favouriteFragment)
        )
        setSupportActionBar(binding.toolbar) //indicarle al activity que queremos usar
                                            // esta toolbar como el action bar de la activity

        setupActionBarWithNavController(navController, appBarConfiguration) //configurar el action bar con nuestro navigation controller
                                                                            //a partir de la configuración de arriba

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp() }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    fun setUpListeners(){

    }

    private fun setCurrentFragment(fragment: Fragment){
    }
}

