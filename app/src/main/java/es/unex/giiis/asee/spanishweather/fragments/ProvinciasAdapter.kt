package es.unex.giiis.asee.spanishweather.fragments

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import es.unex.giiis.asee.spanishweather.database.Provincias


/* -------------------------------------------------------------------------------- */
/* ADAPTER DEL RECYCLERVIEW QUE MANEJA VERTICALMENTE A LAS LISTAS */
/* -------------------------------------------------------------------------------- */

class ProvinciasAdapter(
    private var values: List<Provincias>,
    private val context: Context?,
        ) : RecyclerView.Adapter<ProvinciasAdapter.ShowViewHolder>() {

    private var onMovieClickListener: ((Localidad) -> Unit)? = null
    fun setGameClickListener(listener: (Localidad) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(FragmentCatalogoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context)
    } //infla el layout y crea un ViewHolder, devolviendo como parámetro

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(values[position])
    } //recibe como parámetro un ViewHolder, el cuál se reutilizará para cargar el elemento values[position]

    override fun getItemCount(): Int = values.size //devuelve el número de elementos de la lista

    inner class ShowViewHolder( private val binding: FragmentCatalogoBinding,
                                private val context: Context?,
                                ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lista: CatalogoVideogame) {
            with(binding) {

                tvCategory.text=lista.name //le asignamos a la lista el nombre que le corresponda
                val listaHorizontalAdapter = ListaHorizontalAdapter(lista.videojuegos, context)
                listaHorizontalAdapter.setGameClickListener {
                    onMovieClickListener?.let { click ->
                        click(it)
                    }
                }
                listaHorizontalAdapter.updateData(lista.videojuegos)
                rvListaVideojuegos.adapter= listaHorizontalAdapter

            }
        }
    } //vincula los datos del juego con el layout a través del binding


    fun updateData(values: List<CatalogoVideogame>) {
        this.values = values
        notifyDataSetChanged()
    }
}


