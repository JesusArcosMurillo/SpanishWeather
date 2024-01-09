package es.unex.giiis.asee.spanishweather.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.spanishweather.activities.HomeViewModel
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.FragmentFavouriteBinding
import es.unex.giiis.asee.spanishweather.fragments.adapters.FavouriteAdapter
import es.unex.giiis.asee.spanishweather.utils.UserProvider


class FavouriteFragment : Fragment() {
    private val viewModel: FavouriteViewModel by viewModels { FavouriteViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var adapter: FavouriteAdapter
    private var pueblos: List<Localidad> = emptyList()
    private val binding get() = _binding!!


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
        setUpRecyclerView()

        homeViewModel.user.observe(viewLifecycleOwner) {
                user -> viewModel.user = user
        }
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: FavouriteAdapter) {
        viewModel.locationsInFavourite.observe(viewLifecycleOwner)
        { user -> adapter.updateData(user.pueblos) } //user es el parámetro de la lambda, y recibe valor cuando se produce un cambio
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        adapter = FavouriteAdapter(values = pueblos, context = this.context, _binding=binding)
        adapter.setLocationClickListener {
            homeViewModel.onLocalidadClick(it)
        }
        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            listaPueblosFavoritos.layoutManager = layoutManager
            listaPueblosFavoritos.adapter = adapter
        }
    }
}


