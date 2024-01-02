package es.unex.giiis.asee.spanishweather.fragments

import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.utils.Provincia
import es.unex.giiis.asee.spanishweather.databinding.RecyclerVerticalBinding
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyRegion
import es.unex.giiis.asee.spanishweather.datosestadisticos.aragon
import es.unex.giiis.asee.spanishweather.datosestadisticos.cataluna
import es.unex.giiis.asee.spanishweather.datosestadisticos.extremadura
import es.unex.giiis.asee.spanishweather.datosestadisticos.galicia
import es.unex.giiis.asee.spanishweather.fragments.adapters.ProvinciasAdapter
import es.unex.giiis.asee.spanishweather.utils.CCAAAdapter
import es.unex.giiis.asee.spanishweather.utils.CCAAOption
import es.unex.giiis.asee.spanishweather.utils.UserProvider
import kotlinx.coroutines.launch


class ProvinciasFragment : Fragment() {
    private var _binding: RecyclerVerticalBinding? = null
    private lateinit var usuario: Usuario
    private lateinit var region : DummyRegion
    private var _provincias: List<Provincia> = emptyList()
    private lateinit var adapter: ProvinciasAdapter
    private var isTextViewVisible = true // Guarda el estado de visibilidad
    private lateinit var progressBar: ProgressBar
    private val binding get() = _binding!!
    private lateinit var listener: OnLocalidadClickListener
    private lateinit var animation: ObjectAnimator
    private lateinit var db: SpanishWeatherDatabase
    private lateinit var repository : RepositoryLocalidades

    interface OnLocalidadClickListener {
        fun onLocalidadClick(pueblo: Localidad)
    }

    override fun onResume() {
        super.onResume()
        seleccionarCCCA()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            _binding = RecyclerVerticalBinding.inflate(inflater, container, false)
            progressBar = binding.spinner
            _binding!!.textView2.visibility = if (isTextViewVisible) View.VISIBLE else View.GONE
            return binding.root
        }
        catch (e: Exception) {
        Log.e(TAG, "onCreateView", e);
        throw e;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userProvider = activity as UserProvider
        usuario = userProvider.getUser()
        setUpRecyclerView()
    } //se ejecuta después de que la vista del fragmento haya sido creada y llama al setUpRecyclerView()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = SpanishWeatherDatabase.getInstance(context)!!
        repository = RepositoryLocalidades.getInstance(db.localidadDao(), conexionAPI())
        if (context is OnLocalidadClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " debe implementarse OnShowClickListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun seleccionarCCCA() {

        animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.interpolator = LinearInterpolator()

        //Creamos la lista de las CCAA con sus respectivas banderas
        val comunidades = listOf(
            CCAAOption("Andalucía", R.drawable.andalucia),
            CCAAOption("Aragón", R.drawable.aragon),
            CCAAOption("Principado de Asturias", R.drawable.asturias),
            CCAAOption("Canarias", R.drawable.canarias),
            CCAAOption("Cantabria", R.drawable.cantabria),
            CCAAOption("Castilla y León", R.drawable.castillayleon),
            CCAAOption("Castilla-La Mancha", R.drawable.castillalamancha),
            CCAAOption("Cataluña", R.drawable.cataluna),
            CCAAOption("Comunidad Foral de Navarra", R.drawable.navarra),
            CCAAOption("Comunidad de Madrid", R.drawable.madrid),
            CCAAOption("Comunidad Valenciana", R.drawable.valencia),
            CCAAOption("Extremadura", R.drawable.extremadura),
            CCAAOption("Galicia", R.drawable.galicia),
            CCAAOption("Islas Baleares", R.drawable.baleares),
            CCAAOption("La Rioja", R.drawable.larioja),
            CCAAOption("País Vasco", R.drawable.paisvasco),
            CCAAOption("Región de Murcia", R.drawable.murcia),
            CCAAOption("Ceuta", R.drawable.ceuta),
            CCAAOption("Melilla", R.drawable.melilla)
        )
        val adapter = CCAAAdapter(requireContext(), comunidades)
        binding.autoCompleteTextView.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val adapter = parent.adapter as CCAAAdapter
            val selectedCCAA = adapter.getItem(position)
            //binding.textInputLayout.setStartIconDrawable(selectedCCAA!!.imageResId)

            actualizarContenido()

            // Realizar acciones basadas en la opción seleccionada
            when (selectedCCAA!!.name) {
                "Extremadura" -> {
                    // Acciones específicas para Extremadura
                    region = extremadura
                    animation.duration = 4000 // Duración de la animación
                    llamarAPI()
                    val int = 2
                }
                "Galicia" -> {
                    // Acciones específicas para Galicia
                    animation.duration = 6500 // Duración de la animación
                    region = galicia
                    llamarAPI()
                }
                "Aragón" -> {
                    // Acciones específicas para Galicia
                    region = aragon
                    animation.duration = 5000 // Duración de la animación
                    llamarAPI()
                }
                "Cataluña" -> {
                    // Acciones específicas para Galicia
                    region = cataluna
                    animation.duration = 6000 // Duración de la animación
                    llamarAPI()
                }
            }
        }
    }

    private fun actualizarContenido(){
        isTextViewVisible = false
        binding.textView2.visibility = View.GONE
        adapter.clearData() //se ejecuta cada vez seleccionamos una CCAA nueva. Limpia el recyclerView.
    }

    private fun llamarAPI(){
        progressBar.visibility = View.VISIBLE
        binding.tvCargando.visibility = View.VISIBLE
        animation.start()

        lifecycleScope.launch {
            try {
                _provincias = repository.fetchShows(region) //LLAMADA AL REPOSITORIO
                adapter.updateData(_provincias)
            } catch (error: Throwable) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }finally{
                progressBar.visibility = View.GONE
                binding.tvCargando.visibility = View.GONE
            }
        }
    }


    private fun setUpRecyclerView() {
        adapter = ProvinciasAdapter(values = _provincias, context = this.context)
        adapter.setLocalidadClickListener {
            listener.onLocalidadClick(it)
        }

        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaDeListas.layoutManager = layoutManager
            listaDeListas.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }
}
