package es.unex.giiis.asee.spanishweather.fragments.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.databinding.LocalidadItemListBinding


/* ------------------------------------------------------------------------------------------- */
/* ADAPTER DEL RECYCLERVIEW HORIZONTAL CONTENIDO EN CADA UNA DE LAS LISTAS DE NUESTRO CATÁLOGO */
/* ------------------------------------------------------------------------------------------- */

class LocalidadesAdapter(
    private var values: List<Localidad>,
    private val context: Context?
        ) : RecyclerView.Adapter<LocalidadesAdapter.ShowViewHolder>() {

    private var onLocalidadClickListener: ((Localidad) -> Unit)? = null
    fun setLocalidadClickListener(listener: (Localidad) -> Unit) {
        onLocalidadClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        try {
            val binding =
                LocalidadItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val viewHolder = ShowViewHolder(binding, context)
            Log.d("LocalidadesAdapter", "onCreateViewHolder: View created")
            return viewHolder
        } catch (e: Exception) {
            Log.e("LocalidadesAdapter", "Error inflating view", e)
            throw e
        }
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(values[position])
    } //recibe como parámetro un ViewHolder, el cuál se reutilizará para cargar el elemento values[position]

    override fun getItemCount(): Int = values.size //devuelve el número de elementos de la lista

    inner class ShowViewHolder(
        private val binding: LocalidadItemListBinding,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pueblo: Localidad) {
            Log.d("LocalidadesAdapter", "funcion bind: layout cargado")
            with(binding) {
                tvLocalidad.setOnClickListener {
                    onLocalidadClickListener?.let { click ->
                        click(pueblo)
                    }
                }

                tvLocalidad.text = pueblo.location.name //le asignamos el nombre del pueblo
                tvFecha.text = pueblo.current.last_updated //la fecha
                tvTemperaturaminima.text =
                    "${pueblo.current.precip_mm.toString()}mm" //lo que llueve hoy
                tvTemperaturamaxima.text =
                    "${pueblo.current.temp_c.toString()}º" //la temperatura actual

                if (pueblo.current.is_day == 0) { //si es de noche, cargaremos imágenes nocturnas
                    if (pueblo.current.cloud >= 25) {
                        context?.let {
                            Glide.with(context)
                                .load("http://cdn.weatherapi.com/weather/64x64/night/116.png")
                                .into(itemImg)
                        }
                    } else {
                        context?.let {
                            Glide.with(context)
                                .load("https://cdn.weatherapi.com/weather/64x64/night/113.png")
                                .into(itemImg)
                        }
                    }
                } else { //si es de día, cargaremos imagenes diurnas
                    if (pueblo.current.cloud >= 25) {
                        context?.let {
                            Glide.with(context)
                                .load("https://cdn.weatherapi.com/weather/64x64/day/116.png")
                                .into(itemImg)
                        }
                    } else {
                        context?.let {
                            Glide.with(context)
                                .load("https://cdn.weatherapi.com/weather/64x64/day/113.png")
                                .into(itemImg)
                        }
                    }
                }
            }
        }
    }
}





