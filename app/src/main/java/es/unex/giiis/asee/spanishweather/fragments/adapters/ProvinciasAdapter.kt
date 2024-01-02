package es.unex.giiis.asee.spanishweather.fragments.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.utils.Provincia
import es.unex.giiis.asee.spanishweather.databinding.RecyclerHorizontalBinding


/* -------------------------------------------------------------------------------- */
/* ADAPTER DEL RECYCLERVIEW QUE MANEJA VERTICALMENTE A LAS LISTAS */
/* -------------------------------------------------------------------------------- */

class ProvinciasAdapter(
    private var values: List<Provincia>,
    private val context: Context?,
        ) : RecyclerView.Adapter<ProvinciasAdapter.ShowViewHolder>() {

    private var onLocalidadClickListener: ((Localidad) -> Unit)? = null
    fun setLocalidadClickListener(listener: (Localidad) -> Unit) {
        onLocalidadClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        // Log para verificar la cantidad de elementos en la lista
        Log.d("ProvinciasAdapter", "Number of items in the list: ${values.size}")
        return ShowViewHolder(
            RecyclerHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context)
    } //infla el layout y crea un ViewHolder, devolviendo como parámetro

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(values[position])
    } //recibe como parámetro un ViewHolder, el cuál se reutilizará para cargar el elemento values[position]

    override fun getItemCount(): Int = values.size //devuelve el número de elementos de la lista

    inner class ShowViewHolder( private val binding: RecyclerHorizontalBinding,
                                private val context: Context?,
                                ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lista: Provincia) {
            Log.d("ProvinciasAdapter", "bind: Binding data for ${lista.nombreProvincia}")
            with(binding) {

                tvCategory.text=lista.nombreProvincia //le asignamos a la lista el nombre que le corresponda
                val localidadesAdapter = LocalidadesAdapter(lista.listaLocalidades, context)
                localidadesAdapter.setLocalidadClickListener {
                    onLocalidadClickListener?.let { click ->
                        click(it)
                    }
                }
                rvListaPueblos.adapter= localidadesAdapter

            }
        }
    } //vincula los datos del juego con el layout a través del binding


    fun updateData(values: List<Provincia>) {
        this.values = values
        notifyDataSetChanged()
    }

    fun clearData() { //se ejecuta cuando hemos seleccionado una CCAA y cambiamos a otra
        values = emptyList()
        notifyDataSetChanged()
    }
}


