package es.unex.giiis.asee.spanishweather.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.Provincia
import es.unex.giiis.asee.spanishweather.databinding.RecyclerVerticalBinding
import es.unex.giiis.asee.spanishweather.datosestadisticos.DummyProvincia
import es.unex.giiis.asee.spanishweather.datosestadisticos.aragon
import es.unex.giiis.asee.spanishweather.datosestadisticos.cataluna
import es.unex.giiis.asee.spanishweather.datosestadisticos.extremadura
import es.unex.giiis.asee.spanishweather.datosestadisticos.galicia
import es.unex.giiis.asee.spanishweather.fragments.adapters.ProvinciasAdapter
import es.unex.giiis.asee.spanishweather.utils.CCAAAdapter
import es.unex.giiis.asee.spanishweather.utils.CCAAOption
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProvinciasFragment : Fragment() {
    private var _binding: RecyclerVerticalBinding? = null
    private var provinciasSeleccionadas : List<DummyProvincia> = emptyList()
    private var _provincias: List<Provincia> = emptyList()
    private lateinit var adapter: ProvinciasAdapter
    private var isTextViewVisible = true // Guarda el estado de visibilidad

    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(pueblo: Localidad)
    }

    override fun onResume() {
        super.onResume()
        seleccionarCCCA()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            _binding = RecyclerVerticalBinding.inflate(inflater, container, false)
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
        setUpRecyclerView()
    } //se ejecuta después de que la vista del fragmento haya sido creada y llama al setUpRecyclerView()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnShowClickListener) {
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
                    provinciasSeleccionadas = extremadura
                    llamarAPI()
                }
                "Galicia" -> {
                    // Acciones específicas para Galicia
                    provinciasSeleccionadas = galicia
                    llamarAPI()
                }
                "Aragón" -> {
                    // Acciones específicas para Galicia
                    provinciasSeleccionadas = aragon
                    llamarAPI()
                }
                "Cataluña" -> {
                    // Acciones específicas para Galicia
                    provinciasSeleccionadas = cataluna
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
        binding.spinner.visibility = View.VISIBLE
        binding.tvCargando.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                _provincias = fetchShows().filterNotNull()
                adapter.updateData(_provincias)
            } catch (error: Throwable) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }finally{
                binding.spinner.visibility = View.GONE
                binding.tvCargando.visibility = View.GONE
            }
        }
    }

    private suspend fun fetchShows(): List<Provincia> {
        var conjuntoDeProvincias = mutableListOf<Provincia>() //almacena un conjunto de provincias con los pronosticos

        // Habrá dos for: uno que itere sobre cada una de las provincias de una CCAA
        // y otro que itere sobre las 10 localidades más importantes de cada provincia

        for (provincia in provinciasSeleccionadas) {
            var listaProvincia = mutableListOf<Localidad>() //almacena los pronosticos de los pueblos de una provincia
            for (pueblo in provincia.listaPueblos){
                try {
                    val pronosticoPueblo = conexionAPI().getPronostico(
                        "5083e7829a8b426b868181535231812",pueblo, "yes", 3
                    )
                    // con la sentencia anterior cogemos el pronóstico de un solo pueblo
                    // hay que almacenar en una lista todos los pronósticos de todos los pueblos de una provincia

                    listaProvincia.add(pronosticoPueblo)

                } catch (cause: Throwable) {
                    throw Throwable("Unable to fetch data from API", cause)
                }
            }
            //una vez hemos cogido todos los pronósticos de todos los pueblos de una provincia, los almacenaremos
            //en una lista para finalmente tener una lista de provincias con sus respectivos pronósticos
            //esto se ejecutará cada vez que se recuperen los pronósticos de una provincia completa,
            //tantas veces como provincias haya en una  CCAA

            var prov = Provincia(
                nombreProvincia = provincia.nombreProvincia,
                listaLocalidades = listaProvincia
            )

            conjuntoDeProvincias.add(prov) //añadimos al conjunto de provincias, la provincia con todos los pronosticos
        }
       // adapter.notifyDataSetChanged()
        return conjuntoDeProvincias
    }
    private fun setUpRecyclerView() {
        adapter = ProvinciasAdapter(values = _provincias, context = this.context)
        adapter.setLocalidadClickListener {
            listener.onShowClick(it)
        }

        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaDeListas.layoutManager = layoutManager
            listaDeListas.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }
}
