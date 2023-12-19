package es.unex.giiis.asee.spanishweather.fragments

/**
 * A fragment representing a list of Items.
 */
class CatalogoFragment : Fragment() {

    private var _binding: RecyclerFragmentCatalogoBinding? = null
    private var _catalogos: List<CatalogoVideogame> = emptyList()
    private var prefencias = mutableListOf<String>()
    private val binding get() = _binding!!
    private lateinit var adapter: CatalogoListasAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(juego: Videogame)
    }

    fun getPreferencias (): MutableList<String>{
        return prefencias
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    } //se le llama al crear el argumento, par asignar variables y recuperar argumentos


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val listaDeStrings = arguments?.getStringArrayList("listaDeStrings")?.toMutableList()
        prefencias = listaDeStrings!!
        _binding = RecyclerFragmentCatalogoBinding.inflate(inflater, container, false)
        return binding.root
    } //se llama cuando la vista del fragmento se crea, inflandola y devolviendo la vista raiz


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        lifecycleScope.launch {
            if (_catalogos.isEmpty()) {
                binding.spinner.visibility = View.VISIBLE
                try {
                    _catalogos = fetchShows().filterNotNull()
                    adapter.updateData(_catalogos)
                } catch (error: Throwable) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                } finally {
                    binding.spinner.visibility = View.GONE

                }
            }
        }
    } //se ejecuta después de que la vista del fragmento haya sido creada y llama al setUpRecyclerView()

    private suspend fun fetchShows(): List<CatalogoVideogame> {
        var listaUnica = mutableListOf<VideogameAPI>()
        var conjuntoDeListas = mutableListOf<CatalogoVideogame>()
        var categorias =  mutableListOf<String>()
        categorias = getPreferencias()

        // Iterar sobre cada elemento del vector
        for (string in categorias) {
            try {
                listaUnica = llamadaAPI().getCategoryPlatform(string, "pc")
                //hay que hacer el mapeo aquí obligatoriamente
                var catalogo = CatalogoVideogame(
                    name = string,
                    videojuegos = listaUnica.map(VideogameAPI::toVideogame).toMutableList()
                )
                conjuntoDeListas.add(catalogo)
                delay(1000)  // 1000 milisegundos = 1 segundo
            } catch (causa: Throwable) {
                throw Throwable("Unable to fetch data from API", causa)
            }
        }
        return conjuntoDeListas
    }
    private fun setUpRecyclerView() {
        adapter = CatalogoListasAdapter(values = _catalogos, context = this.context)
        adapter.setGameClickListener {
            listener.onShowClick(it)
        }

        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaDeListas.layoutManager = layoutManager
            listaDeListas.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " debe implementar OnShowClickListener")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscoverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CatalogoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}