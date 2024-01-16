package es.unex.giiis.asee.spanishweather.fragments.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.databinding.FavouriteItemListBinding
import es.unex.giiis.asee.spanishweather.databinding.FragmentFavouriteBinding


/* -------------------------------------------------------------------------------- */
/* ADAPTER DEL RECYCLERVIEW QUE MANEJA VERTICALMENTE A LAS LISTAS */
/* -------------------------------------------------------------------------------- */

class FavouriteAdapter(
    private var values: List<Localidad>,
    private val context: Context?,
    private var _binding: FragmentFavouriteBinding,
        ) : RecyclerView.Adapter<FavouriteAdapter.ShowViewHolder>() {

    private var onLocationClickListener: ((Localidad) -> Unit)? = null
    private var isTextViewVisible = true
    fun setLocationClickListener(listener: (Localidad) -> Unit) {
        onLocationClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(
            FavouriteItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context)
    } //infla el layout y crea un ViewHolder, devolviendo como parámetro

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(values[position])
    } //recibe como parámetro un ViewHolder, el cuál se reutilizará para cargar el elemento values[position]

    override fun getItemCount(): Int = values.size //devuelve el número de elementos de la lista

    inner class ShowViewHolder(private val binding: FavouriteItemListBinding,
                               private val context: Context?)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(pueblo: Localidad) {
            with(binding) {
                nombre.setOnClickListener {
                    onLocationClickListener?.let { click ->
                        click(pueblo)
                    }
                }
                nombre.text ="${pueblo.location.name}, "  //le asignamos el nombre del pueblo
                region.text = pueblo.location.region
            }
        }
    }

    private fun actualizarTexto(){
        if (values.isEmpty()){
            _binding.tvFavoritos.visibility = View.VISIBLE
        }else{
            _binding.tvFavoritos.visibility = View.GONE
        }

    }

    fun updateData(values: List<Localidad>) {
        this.values = values
        notifyDataSetChanged()
        actualizarTexto()
    }
}


