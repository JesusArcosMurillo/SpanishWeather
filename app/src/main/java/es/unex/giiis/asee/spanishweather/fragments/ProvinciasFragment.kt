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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.spanishweather.R
import es.unex.giiis.asee.spanishweather.activities.HomeViewModel
import es.unex.giiis.asee.spanishweather.api.models.Localidad
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
    private val viewModel: ProvinciasViewModel by viewModels { ProvinciasViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: RecyclerVerticalBinding? = null
    private var region : DummyRegion? = null
    private var _provincias: List<Provincia> = emptyList()
    private lateinit var adapter: ProvinciasAdapter
    private var isTextViewVisible = true // Guarda el estado de visibilidad
    private lateinit var progressBar: ProgressBar
    private val binding get() = _binding!!
    private lateinit var animation: ObjectAnimator

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
        setUpRecyclerView()

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        homeViewModel.localidad.observe(viewLifecycleOwner) { localidad ->
            if (localidad != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val localidadBuscada = viewModel.buscar(localidad)
                    if (localidadBuscada != null) {
                        var conjuntoDeProvincias = mutableListOf<Provincia>()
                        var conjuntoDePueblos = mutableListOf<Localidad>()
                        conjuntoDePueblos.add(localidadBuscada)
                        conjuntoDeProvincias.add(
                            Provincia(
                                nombreProvincia = localidadBuscada.location.region, null,
                                listaLocalidades = conjuntoDePueblos
                            )
                        )
                        var _provincias: List<Provincia> = emptyList()
                        adapter.updateData(conjuntoDeProvincias)
                    }
                }
            }
        }

        viewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }

        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        subscribeUi(adapter)

        // El subscribeUi se ejecuta antes de haber seleccionado la CCAA, por lo que,
        // estará esperando al LiveData para actualizar la pantalla cuando se seleccione una CCAA.
        // El intentar actualizar de la API se realiza cada vez que selecciono una CCAA de la lista
        // desplegable, pero, imaginemos que hemos seleccionado una CCAA, se carga de la API,
        // vamos a otro fragmento, volvemos al original y nos muestra información desactualizada
        // (se ha superado el tiempo de la cache). En ese caso, sólo podríamos volver a mostrar información
        // si seleccionamos de nuevo esa CCAA en el desplegable. Pero no queremos eso, queremos que se haga automáticamente.

        if (region != null) { //si ya se ha seleccionado una region
            viewModel.setRegion(region!!.nombreRegion)
            viewModel.refresh(region!!)
        }
    }


    private fun subscribeUi(adapter: ProvinciasAdapter) {
        viewModel.localidades.observe(viewLifecycleOwner) { localidades ->
            /**
             * Me dan una lista de localidades filtradas por la region
             * En el recycler view mostraré todas las localidades de una región
             * No obstante, no lo mostraré como una lista vertical de localidades,
             * sino, que, estará organizado por provincias. Imaginemos que yo filtro
             * por las localidades de Extremadura: tengo que mostrar dos RecyclerView
             * horizontales, uno para Cáceres y otro para Badajoz.
             * Para hacer esto, llamaré al método generarListas(), el cuál me devolverá
             * una lista de provincias con sus respectivas localidades
             */


            val provincias = generarListas(localidades)
            adapter.updateData(provincias)
        }
    } //se ejecuta después de que la vista del fragmento haya sido creada y llama al setUpRecyclerView()

    private fun generarListas(pueblosRegion : List<Localidad>) : List<Provincia> {
        var conjuntoDeProvincias = mutableListOf<Provincia>() //almacena un conjunto de provincias con los pronosticos

        for (provincia in region!!.listaProvincias){
            var prov = Provincia(
                nombreProvincia = provincia.nombreProvincia,
                region = region!!.nombreRegion,
                listaLocalidades = pueblosRegion.filter { it.provincia == provincia.nombreProvincia }.toMutableList()
             )
            conjuntoDeProvincias.add(prov) //añadimos al conjunto de provincias, la provincia con todos los pronosticos
        }
        return conjuntoDeProvincias
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
            CCAAOption("Aragón", R.drawable.aragon),
            CCAAOption("Cataluña", R.drawable.cataluna),
            CCAAOption("Extremadura", R.drawable.extremadura),
            CCAAOption("Galicia", R.drawable.galicia),
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
                    viewModel.setRegion("Extremadura") //al poner la region, obtendremos a través del LiveData en
                                                        //subscribeUi las localidades por region
                    viewModel.refresh(region!!)

                }
                "Galicia" -> {
                    // Acciones específicas para Galicia
                    animation.duration = 6500 // Duración de la animación
                    region = galicia
                    viewModel.setRegion("Galicia") //al poner la region, obtendremos a través del LiveData en
                                                    //subscribeUi las localidades por region
                    viewModel.refresh(region!!)

                }
                "Aragón" -> {
                    // Acciones específicas para Galicia
                    region = aragon
                    animation.duration = 5000 // Duración de la animación
                    viewModel.setRegion("Aragon")  //al poner la region, obtendremos a través del LiveData en
                                                    //subscribeUi las localidades por region
                    viewModel.refresh(region!!)

                }
                "Cataluña" -> {
                    // Acciones específicas para Galicia
                    region = cataluna
                    animation.duration = 6000 // Duración de la animación
                    viewModel.setRegion("Catalonia")
                    viewModel.refresh(region!!)

                }
            }
        }
    }

    private fun actualizarContenido(){
        isTextViewVisible = false
        binding.textView2.visibility = View.GONE
        adapter.clearData() //se ejecuta cada vez seleccionamos una CCAA nueva. Limpia el recyclerView.
    }


    private fun setUpRecyclerView() {
        adapter = ProvinciasAdapter(values = _provincias, context = this.context)
        adapter.setLocalidadClickListener {
            homeViewModel.onLocalidadClick(it)
        }

        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaDeListas.layoutManager = layoutManager
            listaDeListas.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }
}
