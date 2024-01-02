package es.unex.giiis.asee.spanishweather.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import es.unex.giiis.asee.spanishweather.api.conexionAPI
import es.unex.giiis.asee.spanishweather.api.models.Localidad
import es.unex.giiis.asee.spanishweather.database.RepositoryLocalidades
import es.unex.giiis.asee.spanishweather.database.SpanishWeatherDatabase
import es.unex.giiis.asee.spanishweather.database.clases.Usuario
import es.unex.giiis.asee.spanishweather.databinding.FragmentDetailBinding
import es.unex.giiis.asee.spanishweather.utils.UserProvider
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class DetailFragment : Fragment() {
    private var _binding:FragmentDetailBinding? = null
    private lateinit var db: SpanishWeatherDatabase
    private lateinit var usuario: Usuario
    private val binding get() = _binding!!
    private lateinit var repository : RepositoryLocalidades
    private val args: DetailFragmentArgs by navArgs()

    companion object {
        fun newInstance(localidad: Localidad): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable("localidad", localidad)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = SpanishWeatherDatabase.getInstance(context)!!
        repository = RepositoryLocalidades.getInstance(db.localidadDao(),conexionAPI())
    }

    private fun onFavoriteButtonClick() {
        lifecycleScope.launch {
            val localidad = args.localidad.location
            repository.setFavorite(localidad, usuario) //LLAMADA AL REPOSITORY
            Toast.makeText(requireContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onNoFavoriteButtonClick(){
        lifecycleScope.launch {
            val localidad = args.localidad.location
            repository.setNoFavorite(localidad) //LLAMADA AL REPOSITORY
            Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localidad = args.localidad
        val userProvider = activity as UserProvider
        usuario = userProvider.getUser()

        val hora = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH") // Utiliza "HH" para obtener solo las horas
        var horaActual = hora.format(formatter).toInt()  //sacamos la hora actual para poder acceder a la hora adecuada en el vector

        binding.puebloFav.isChecked = localidad.location.is_favourite
        binding.puebloFav.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onFavoriteButtonClick()
                } else {
                    onNoFavoriteButtonClick()
                }
            }

        binding.textView24.text = "${localidad.forecast.forecastday[2].day.daily_chance_of_rain.toString()}%"
        binding.textView25.text = obtenerDiaSemana(localidad.forecast.forecastday[2].date)
        binding.textView27.text = round(localidad.forecast.forecastday[2].day.mintemp_c).toString()
        binding.textView26.text = round(localidad.forecast.forecastday[2].day.maxtemp_c).toString()
        binding.tvNombre.text = localidad.location.name
        binding.tvTpactual.text = localidad.current.temp_c.toInt().toString() //le quitamos los decimales
        binding.textView6.text = localidad.current.condition.text
        binding.textView10.text = localidad.forecast.forecastday[0].hour[horaActual].temp_c.toInt().toString()
        binding.tvAmanecer.text = convertirFormato24Horas(localidad.forecast.forecastday[0].astro.sunrise)
        binding.tvAmanecer7.text = convertirFormato24Horas(localidad.forecast.forecastday[0].astro.sunset)

        if (horaActual<20){
            binding.tvAmanecer3.text =
                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 1].time)
            binding.tvAmanecer4.text =
                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 2].time)
            binding.tvAmanecer5.text =
                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 3].time)
            binding.tvAmanecer6.text =
                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 4].time)

            binding.textView8.text = localidad.forecast.forecastday[0].hour[horaActual+1].temp_c.toInt().toString()
            binding.textView5.text = localidad.forecast.forecastday[0].hour[horaActual+2].temp_c.toInt().toString()
            binding.textView9.text = localidad.forecast.forecastday[0].hour[horaActual+3].temp_c.toInt().toString()
            binding.textView7.text = localidad.forecast.forecastday[0].hour[horaActual+4].temp_c.toInt().toString()

            context?.let {
                Glide.with(requireContext())
                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+1].condition.icon}")
                    .into(binding.imageView6)
            }

            context?.let {
                Glide.with(requireContext())
                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+2].condition.icon}")
                    .into(binding.imageView7)
            }

            context?.let {
                Glide.with(requireContext())
                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+3].condition.icon}")
                    .into(binding.imageView8)
            }

            context?.let {
                Glide.with(requireContext())
                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+4].condition.icon}")
                    .into(binding.imageView9)
            }

        }else{
            if (horaActual>=23) {
                binding.tvAmanecer3.text = obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[0].time)
                binding.tvAmanecer4.text = obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[1].time)
                binding.tvAmanecer5.text = obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[2].time)
                binding.tvAmanecer6.text = obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[3].time)

                binding.textView8.text = localidad.forecast.forecastday[1].hour[0].temp_c.toInt().toString()
                binding.textView5.text = localidad.forecast.forecastday[1].hour[1].temp_c.toInt().toString()
                binding.textView9.text = localidad.forecast.forecastday[1].hour[2].temp_c.toInt().toString()
                binding.textView7.text = localidad.forecast.forecastday[1].hour[3].temp_c.toInt().toString()

                context?.let {
                    Glide.with(requireContext())
                        .load("https:${localidad.forecast.forecastday[1].hour[0].condition.icon}")
                        .into(binding.imageView6)
                }

                context?.let {
                    Glide.with(requireContext())
                        .load("https:${localidad.forecast.forecastday[1].hour[1].condition.icon}")
                        .into(binding.imageView7)
                }

                context?.let {
                    Glide.with(requireContext())
                        .load("https:${localidad.forecast.forecastday[1].hour[2].condition.icon}")
                        .into(binding.imageView8)
                }

                context?.let {
                    Glide.with(requireContext())
                        .load("https:${localidad.forecast.forecastday[1].hour[3].condition.icon}")
                        .into(binding.imageView9)
                }
            }else {
                if (horaActual >= 22) {
                    binding.tvAmanecer3.text =
                        obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 1].time)
                    binding.tvAmanecer4.text =
                        obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[0].time)
                    binding.tvAmanecer5.text =
                        obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[1].time)
                    binding.tvAmanecer6.text =
                        obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[2].time)

                    binding.textView8.text = localidad.forecast.forecastday[0].hour[horaActual+1].temp_c.toInt().toString()
                    binding.textView5.text = localidad.forecast.forecastday[1].hour[0].temp_c.toInt().toString()
                    binding.textView9.text = localidad.forecast.forecastday[1].hour[1].temp_c.toInt().toString()
                    binding.textView7.text = localidad.forecast.forecastday[1].hour[2].temp_c.toInt().toString()

                    context?.let {
                        Glide.with(requireContext())
                            .load("https:${localidad.forecast.forecastday[0].hour[horaActual+1].condition.icon}")
                            .into(binding.imageView6)
                    }

                    context?.let {
                        Glide.with(requireContext())
                            .load("https:${localidad.forecast.forecastday[1].hour[0].condition.icon}")
                            .into(binding.imageView7)
                    }

                    context?.let {
                        Glide.with(requireContext())
                            .load("https:${localidad.forecast.forecastday[1].hour[1].condition.icon}")
                            .into(binding.imageView8)
                    }

                    context?.let {
                        Glide.with(requireContext())
                            .load("https:${localidad.forecast.forecastday[1].hour[2].condition.icon}")
                            .into(binding.imageView9)
                    }
                } else {
                    if (horaActual >= 21) {
                        binding.tvAmanecer3.text =
                            obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 1].time)
                        binding.tvAmanecer4.text =
                            obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 2].time)
                        binding.tvAmanecer5.text =
                            obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[0].time)
                        binding.tvAmanecer6.text =
                            obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[1].time)

                        binding.textView8.text = localidad.forecast.forecastday[0].hour[horaActual+1].temp_c.toInt().toString()
                        binding.textView5.text = localidad.forecast.forecastday[0].hour[horaActual+2].temp_c.toInt().toString()
                        binding.textView9.text = localidad.forecast.forecastday[1].hour[0].temp_c.toInt().toString()
                        binding.textView7.text = localidad.forecast.forecastday[1].hour[1].temp_c.toInt().toString()

                        context?.let {
                            Glide.with(requireContext())
                                .load("https:${localidad.forecast.forecastday[0].hour[horaActual+1].condition.icon}")
                                .into(binding.imageView6)
                        }

                        context?.let {
                            Glide.with(requireContext())
                                .load("https:${localidad.forecast.forecastday[0].hour[horaActual+2].condition.icon}")
                                .into(binding.imageView7)
                        }

                        context?.let {
                            Glide.with(requireContext())
                                .load("https:${localidad.forecast.forecastday[1].hour[0].condition.icon}")
                                .into(binding.imageView8)
                        }

                        context?.let {
                            Glide.with(requireContext())
                                .load("https:${localidad.forecast.forecastday[1].hour[1].condition.icon}")
                                .into(binding.imageView9)
                        }
                    } else {
                        if (horaActual >= 20) {
                            binding.tvAmanecer3.text =
                                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 1].time)
                            binding.tvAmanecer4.text =
                                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 2].time)
                            binding.tvAmanecer5.text =
                                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[0].hour[horaActual + 3].time)
                            binding.tvAmanecer6.text =
                                obtenerHoraDesdeCadenaTiempo(localidad.forecast.forecastday[1].hour[0].time)

                            binding.textView8.text = localidad.forecast.forecastday[0].hour[horaActual+1].temp_c.toInt().toString()
                            binding.textView5.text = localidad.forecast.forecastday[0].hour[horaActual+2].temp_c.toInt().toString()
                            binding.textView9.text = localidad.forecast.forecastday[0].hour[horaActual+3].temp_c.toInt().toString()
                            binding.textView7.text = localidad.forecast.forecastday[1].hour[0].temp_c.toInt().toString()

                            context?.let {
                                Glide.with(requireContext())
                                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+1].condition.icon}")
                                    .into(binding.imageView6)
                            }

                            context?.let {
                                Glide.with(requireContext())
                                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+2].condition.icon}")
                                    .into(binding.imageView7)
                            }

                            context?.let {
                                Glide.with(requireContext())
                                    .load("https:${localidad.forecast.forecastday[0].hour[horaActual+3].condition.icon}")
                                    .into(binding.imageView8)
                            }

                            context?.let {
                                Glide.with(requireContext())
                                    .load("https:${localidad.forecast.forecastday[1].hour[0].condition.icon}")
                                    .into(binding.imageView9)
                            }
                        }
                    }
                }
            }
        }

        binding.textView34.text = round(localidad.current.air_quality.co).toString()
        binding.textView35.text = round(localidad.current.air_quality.o3).toString()
        binding.textView36.text = round(localidad.current.air_quality.no2).toString()
        binding.textView20.text = obtenerDiaSemana(localidad.forecast.forecastday[1].date)
        binding.textView23.text = "${localidad.forecast.forecastday[1].day.daily_chance_of_rain.toString()}%"
        binding.textView21.text = round(localidad.forecast.forecastday[1].day.mintemp_c).toString()
        binding.textView22.text = round(localidad.forecast.forecastday[1].day.maxtemp_c).toString()
        binding.textView19.text = "${localidad.forecast.forecastday[0].day.daily_chance_of_rain.toString()}%"
        binding.textView14.text = round(localidad.forecast.forecastday[0].day.mintemp_c).toString()
        binding.textView15.text = round(localidad.forecast.forecastday[0].day.maxtemp_c).toString()

        //Ahora, vincularemos las imágenes
        context?.let {
            Glide.with(requireContext())
                .load("https:${localidad.forecast.forecastday[0].hour[horaActual].condition.icon}")
                .into(binding.imageView5)
        }


        context?.let {
            Glide.with(requireContext())
                .load("https:${localidad.forecast.forecastday[0].day.condition.icon}")
                .into(binding.imageView11)
        }

        context?.let {
            Glide.with(requireContext())
                .load("https:${localidad.forecast.forecastday[2].day.condition.icon}")
                .into(binding.imageView13)
        }

        context?.let {
            Glide.with(requireContext())
                .load("https:${localidad.forecast.forecastday[1].day.condition.icon}")
                .into(binding.imageView14)
        }
    }



    private fun obtenerDiaSemana(fechaString: String): String {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {

            val fecha = formatoFecha.parse(fechaString) // convertimos la cadena de fecha a un objeto Date
            val calendario = Calendar.getInstance() // se obtiene el día de la semana
            calendario.time = fecha
            val diaSemana = calendario.get(Calendar.DAY_OF_WEEK)
            val nombresDias = arrayOf("", "Domingo", "Lunes",
                "Martes", "Miércoles", "Jueves", "Viernes", "Sábado") // mapear el número del día de la semana a un nombre
            val dia = nombresDias[diaSemana]

            return dia.take(3) //devolvemos las 3 primeras letras
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return "ERROR AL OBTENER DIA DE LA SEMANA"
    }

    fun obtenerHoraDesdeCadenaTiempo(cadenaTiempo: String): String {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("HH", Locale.getDefault())

        try {
            val fechaHora = formatoEntrada.parse(cadenaTiempo)
            return formatoSalida.format(fechaHora)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return "ERROR AL MAPEAR LA HORA"
    }

    private fun convertirFormato24Horas(horaAMPM: String): String {
        val AMoPM = horaAMPM.takeLast(2)
        var horaSalida = "ERROR AL CONVERTIR AM/PM"

        if (AMoPM.equals("AM", ignoreCase = true)) {
                horaSalida = horaAMPM.take(5) //si es AM, no habrá que realizar operaciones especiales
        } else {
            if (AMoPM.equals("PM", ignoreCase = true)) {
                val calculo = horaAMPM.take(2).toInt() + 12 // si es PM, sumamos doce a las horas
                horaSalida = "$calculo${horaAMPM.substring(2, 5)}"
            }
        }
        return horaSalida
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}