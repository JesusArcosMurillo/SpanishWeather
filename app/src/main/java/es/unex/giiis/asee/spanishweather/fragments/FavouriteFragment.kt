package es.unex.giiis.asee.spanishweather.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.database.clases.Location
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.FragmentFavouriteBinding
import es.unex.giiis.asee.spanishweather.fragments.adapters.FavouriteAdapter
import es.unex.giiis.asee.spanishweather.utils.UserProvider


class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var adapter: FavouriteAdapter
    private lateinit var listener: OnLocationClickListener
    private var pueblos: List<Location> = emptyList()
    private lateinit var usuario: Usuario
    private val binding get() = _binding!!
    private lateinit var db: SpanishWeatherDatabase
    private lateinit var repository : RepositoryLocalidades

    interface OnLocationClickListener {
        fun onLocationClick(pueblo: Location)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
            return binding.root
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreateView", e);
            throw e;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userProvider = activity as UserProvider
        usuario = userProvider.getUser()
        setUpRecyclerView()
        subscribeUi(adapter)
        repository.setUserName(usuario.userName)
    }

    private fun subscribeUi(adapter: FavouriteAdapter) {
        repository.locationsInFavourite.observe(viewLifecycleOwner)
        { user -> adapter.updateData(user.pueblos) } //user es el parámetro de la lambda, y recibe valor cuando se produce un cambio
    }


    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = SpanishWeatherDatabase.getInstance(context)!!
        repository = RepositoryLocalidades.getInstance(db.localidadDao(), conexionAPI())
        if (context is OnLocationClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " debe implementarse OnLocationClickListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        adapter = FavouriteAdapter(values = pueblos, context = this.context, _binding=binding)
        adapter.setLocationClickListener {
            listener.onLocationClick(it)
        }
        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaPueblosFavoritos.layoutManager = layoutManager
            listaPueblosFavoritos.adapter = adapter
        }
    }
}


