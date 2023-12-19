package es.unex.giiis.asee.spanishweather.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.content.Context



/* ------------------------------------------------------------------------------------------- */
/* ADAPTER DEL RECYCLERVIEW HORIZONTAL CONTENIDO EN CADA UNA DE LAS LISTAS DE NUESTRO CATÁLOGO */
/* ------------------------------------------------------------------------------------------- */

class ListaHorizontalAdapter(
    private var values: List<Videogame>,
    private val context: Context?
        ) : RecyclerView.Adapter<ListaHorizontalAdapter.ShowViewHolder>() {

    private var onMovieClickListener: ((Videogame) -> Unit)? = null
    fun setGameClickListener(listener: (Videogame) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(CatalogoItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context)
    } //infla el layout y crea un ViewHolder, devolviendo como parámetro

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(values[position])
    } //recibe como parámetro un ViewHolder, el cuál se reutilizará para cargar el elemento values[position]

    override fun getItemCount(): Int = values.size //devuelve el número de elementos de la lista

    inner class ShowViewHolder( private val binding: CatalogoItemListBinding,
                                private val context: Context?)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(juego: Videogame) {
            with(binding) {
                btImg.setOnClickListener {
                    onMovieClickListener?.let { click ->
                        click(juego)
                    }
                }

                context?.let {
                    Glide.with(context)
                        .load(juego.image)
                        //.apply(RequestOptions().override(50,50))
                        .into(btImg)
                }
            }
        }
    }
    fun updateData(values: List<Videogame>) {
        this.values = values
        notifyDataSetChanged()
    }
        } //vincula los datos del juego con el layout a través del binding



